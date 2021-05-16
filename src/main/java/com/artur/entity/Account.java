package com.artur.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
public class Account extends AbstractEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToOne(mappedBy = "account")
    private VerificationToken verificationToken;

    @OneToMany(mappedBy = "account")
    private List<UserOrder> userOrders;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    private void setEnabled() {
        enabled = false;
    }

    public void addOrder(UserOrder userOrder) {
        userOrders.add(userOrder);
        userOrder.setAccount(this);
    }
    public void removeOrder(UserOrder userOrder) {
        userOrders.remove(userOrder);
        userOrder.setAccount(null);
    }

}
