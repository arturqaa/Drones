package com.artur.security;

import com.artur.entity.Account;
import com.artur.repo.AccountRepository;
import com.artur.exception.BadRequestException;
import com.artur.exception.ResourceNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public CustomUserDetailsService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @SneakyThrows
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {

        Optional<Account> userOptional = accountRepository.findByLoginOrEmail(login, login);
        if (userOptional.isEmpty()) {
            log.info("No user found with username: " + login);
            throw new UsernameNotFoundException("No user found with username: " + login);
        }

        if (!userOptional.get().isEnabled()) {
            log.info("Account not activated");
            throw new BadRequestException("Account not activated");
        }

        return com.artur.security.UserPrincipal.create(userOptional.get());
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account", "id", id)
        );
        return com.artur.security.UserPrincipal.create(account);
    }
}
