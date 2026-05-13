package com.gamehok.tournament.notification.dto;

import com.gamehok.tournament.enums.NotificationChannel;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Notification response DTO.
 */
@Getter
@Builder
public class NotificationDto {

    private final UUID uuid;
    private final String title;
    private final String body;
    private final String type;
    private final NotificationChannel channel;
    private final boolean read;
    private final Instant readAt;
    private final Instant createdAt;
    private final String referenceType;
    private final Long referenceId;
}
