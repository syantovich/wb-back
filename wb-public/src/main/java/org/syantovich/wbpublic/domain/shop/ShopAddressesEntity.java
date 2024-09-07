// ShopAddresses.java
package org.syantovich.wbpublic.domain.shop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "shop_addresses")
@Getter
@Setter
public class ShopAddressesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //координаты магазина
    @Column(nullable = false)
    private String latitude;
    @Column(nullable = false)
    private String longitude;

    //адрес магазина
    private String city;
    private String street;
    private String house;
    private String building;
    private String apartment;
    private String entrance;
    private String floor;
    private String room;

    //Является ли адрес пунктом самовывоза
    private Boolean isPickupPoint;

    //График работы магазина
    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ShopWorkScheduleEntity workSchedule;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;
}