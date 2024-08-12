package org.syantovich.wbpublic.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.syantovich.wbpublic.enums.Authorities;

import java.util.*;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "persons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(nullable = false)
    String email;
    String name;
    String password;
    @Column(name = "is_verified")
    Boolean isVerified;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = ALL)
    List<AuthorityEntity> perms;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (perms == null) {
            return List.of();
        }

        var authorities = new ArrayList<GrantedAuthority>();
        for (var perm : perms) {
            var auth = new SimpleGrantedAuthority(perm.getName());
            authorities.add(auth);
        }

        authorities.add(new SimpleGrantedAuthority(isVerified?Authorities.VERIFIED.toString():Authorities.UNVERIFIED.toString()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

}
