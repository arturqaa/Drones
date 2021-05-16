package com.artur.service;

import com.artur.entity.Account;
import com.artur.service.dto.AccountDto;
import com.artur.service.dto.PasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountDto getAccountByLoginAndPassword(AccountDto accountDto);

    AccountDto registerNewUserAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    void createVerificationToken(Account account, String token);

    void confirmRegistration(String token);

    Page<AccountDto> getAllAccountsForAdmin(Pageable pageable);

    String resetPassword(String email);

    void createPasswordResetTokenForUser(Account account, String token);

    String confirmChangePassword(String token);

    void saveNewPassword(PasswordDto passwordDto, Long id);

    void deleteAccountById(Long id);
}
