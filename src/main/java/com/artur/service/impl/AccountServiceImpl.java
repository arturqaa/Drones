package com.artur.service.impl;


import com.artur.entity.Account;
import com.artur.service.SendNewPasswordEven;
import com.artur.service.AccountService;
import com.artur.service.dto.AccountDto;
import com.artur.service.mapper.UserOrderMapper;
import com.artur.service.mapper.AccountMapper;
import com.artur.entity.Role;
import com.artur.entity.VerificationToken;
import com.artur.exception.BadRequestException;
import com.artur.exception.ResourceNotFoundException;
import com.artur.exception.UserAlreadyExistException;
import com.artur.repo.AccountRepository;
import com.artur.repo.RoleRepository;
import com.artur.repo.VerificationTokenRepository;
import com.artur.service.OnRegistrationCompleteEvent;
import com.artur.service.OnResetPasswordEven;
import com.artur.service.dto.PasswordDto;
import com.artur.types.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserUserOrderServiceImpl orderService;
    private final UserOrderMapper userOrderMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, ApplicationEventPublisher eventPublisher, VerificationTokenRepository tokenRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserUserOrderServiceImpl orderService, UserOrderMapper userOrderMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.eventPublisher = eventPublisher;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.orderService = orderService;
        this.userOrderMapper = userOrderMapper;
    }

    @Override
    public AccountDto getAccountByLoginAndPassword(AccountDto accountDto) {
        Optional<Account> accountOptional = accountRepository.findByLoginAndPassword(accountDto.getLogin(), accountDto.getPassword());
        if (accountOptional.isEmpty()) {
            log.info("User with not found.");
            throw new ResourceNotFoundException("User with not found.");
        }
        return accountMapper.toDto(accountOptional.get());
    }

    @Transactional
    @Override
    public AccountDto registerNewUserAccount(AccountDto accountDto) {
        Optional<Account> accountOptional = accountRepository.findByEmail(accountDto.getEmail());
        if (accountOptional.isPresent()) {
            throw new UserAlreadyExistException(accountDto.getEmail());
        } else {
            accountOptional = accountRepository.findByLogin(accountDto.getLogin());
            if (accountOptional.isPresent()) {
                throw new UserAlreadyExistException(accountDto.getLogin());
            }
        }
        Account accountEntity = accountMapper.toEntity(accountDto);
        accountEntity.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Role roleEntity = roleRepository.findByRoleName(RoleType.USER).orElseThrow(
                () -> new ResourceNotFoundException("Role user not found."));
        accountEntity.setRole(roleEntity);
        accountEntity = accountRepository.save(accountEntity);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(accountEntity));
        orderService.createOrderForUser(accountEntity);
        return accountMapper.toDto(accountEntity);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Optional<Account> userOptional = accountRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account", "id", id);
        }
        return accountMapper.toDto(userOptional.get());
    }

    @Override
    public void createVerificationToken(Account account, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setToken(token);
        myToken.setAccount(account);
        tokenRepository.save(myToken);
    }

    @Override
    public void confirmRegistration(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new ResourceNotFoundException(token);
        }
        Account account = verificationToken.getAccount();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new BadRequestException("Срок действия токина истек.");
        }
        account.setEnabled(true);
        accountRepository.save(account);
    }

    @Override
    public Page<AccountDto> getAllAccountsForAdmin(Pageable pageable) {
        Page<Account> userPage = accountRepository.findAll(pageable);
        return userPage.map(accountMapper::toDto);
    }

    @Override
    public String resetPassword(String email) {
        Account accountEntity = accountRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Account", "email", email));
        eventPublisher.publishEvent(new OnResetPasswordEven(accountEntity));
        return null;
    }

    @Override
    public void createPasswordResetTokenForUser(Account account, String token) {
        VerificationToken myToken = tokenRepository.findByAccountId(account.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Account", "id", account.getId()));
        myToken.setToken(token);
        myToken.setAccount(account);
        tokenRepository.save(myToken);
    }

    @Override
    public String confirmChangePassword(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new ResourceNotFoundException(token);
        }
        Account accountEntity = verificationToken.getAccount();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new BadRequestException("Срок действия токина истек.");
        }
        accountEntity.setEnabled(true);
        String newPassword = generateRandomSpecialCharacters(6);
        accountEntity.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(accountEntity);
        eventPublisher.publishEvent(new SendNewPasswordEven(accountEntity, newPassword));
        return token;
    }

    @Override
    public void saveNewPassword(PasswordDto passwordDto, Long userId) {
        Optional<Account> userEntity = accountRepository.findById(userId);
        if (userEntity.isPresent()){
            if (!passwordEncoder.matches(passwordDto.getOldPassword(), userEntity.get().getPassword())){
                throw new BadRequestException("Старый пароль не верный.");
            }
        }

        userEntity.get().setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        accountRepository.save(userEntity.get());
    }

    private String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        return pwdGenerator.generate(length);
    }

    public void deleteAccountById(Long id){
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()){
            throw new BadRequestException("User not found");
        }
        accountRepository.delete(accountOptional.get());
    }
}
