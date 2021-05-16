package com.artur.service;

import com.artur.entity.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnResetPasswordEven extends ApplicationEvent {
    private Account account;

    public OnResetPasswordEven(Account account) {
        super(account);
        this.account = account;
    }
}
