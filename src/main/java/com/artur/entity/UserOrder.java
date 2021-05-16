package com.artur.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
public class UserOrder extends AbstractEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_order_id_seq")
    @SequenceGenerator(name = "user_order_id_seq", sequenceName = "user_order_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Account account;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "status_id")
    private Status status;

    public UserOrder(){}

    public UserOrder(Account account) {
        this.setAccount(account);
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.getUserOrders().add(this);
    }
    public void removeProduct(Product product){
        this.products.remove(product);
        product.getUserOrders().remove(this);
    }
}
