package com.kacper.backend.mail;

import lombok.Builder;

/**
 * @param subject is the subject of the email
 * @param message is the message of the email
 *
 * @Author Kacper Karabinowski
 */
@Builder
public record MailStructure(
        String subject,
        String message
) {
}
