package org.syantovich.wbpublic.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SendVerifiedCodeDto {
    private UUID personId;
}
