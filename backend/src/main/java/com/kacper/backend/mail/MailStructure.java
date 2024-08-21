package com.kacper.backend.mail;

import lombok.Builder;

@Builder
public record MailStructure(
        String subject,
        String message
) {
}
