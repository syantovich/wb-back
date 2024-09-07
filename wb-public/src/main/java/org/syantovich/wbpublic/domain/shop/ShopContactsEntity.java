package org.syantovich.wbpublic.domain.shop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.syantovich.wbpublic.enums.EContacts;

import java.util.UUID;

@Entity
@Table(name = "shop_contacts")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopContactsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    EContacts type; // Тип контакта

    @Column(nullable = false)
    String value; // Значение контакта

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    ShopEntity shop;
}