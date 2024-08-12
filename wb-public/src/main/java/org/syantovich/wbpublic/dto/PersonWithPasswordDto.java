package org.syantovich.wbpublic.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonWithPasswordDto {
    UUID id;
    String name;
    String email;
    List<String> authorities;
    String password;
    Boolean isVerified;
}
