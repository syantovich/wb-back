package org.syantovich.wbpublic.domain.shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="shops")
@Getter
@Setter
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    //logo магазина
    @Column(name = "logo_url")
    private String logoUrl;

    //Описание магазина
    private String description;

    //Адреса магазина (один ко многим)
    @OneToMany(mappedBy = "shop", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopAddressesEntity> addresses;

    @OneToMany(mappedBy = "shop", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopContactsEntity> contacts;
}

