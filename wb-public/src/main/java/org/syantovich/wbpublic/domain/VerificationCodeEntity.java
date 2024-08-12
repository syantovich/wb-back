package org.syantovich.wbpublic.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String code;
    @Column(name = "expiry_date")
    Date expiryDate;
    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    @ManyToOne
    @JoinColumn(name = "person_id")
    PersonEntity person;
}
