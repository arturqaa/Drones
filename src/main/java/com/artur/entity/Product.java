package com.artur.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
public class Product extends AbstractEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @ManyToMany
    @JoinTable(name = "order_product", joinColumns = @JoinColumn(name = "products_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "orders_id" , referencedColumnName = "id"))
    private Set<UserOrder> userOrders;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "category_id", unique = true)
    private Category category;

    @Column(name = "photo_path")
    private String photoPath;
}
