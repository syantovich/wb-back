package org.syantovich.wbpublic.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String token;
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @OneToMany(mappedBy = "refreshToken", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccessToken> accessTokens;
}
