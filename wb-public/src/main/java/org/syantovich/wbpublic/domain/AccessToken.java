package org.syantovich.wbpublic.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "access_tokens")
@ToString(exclude = "refreshToken")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String token;
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;
}
