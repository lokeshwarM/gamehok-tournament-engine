package com.gamehok.tournament.notification.service;

import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.enums.NotificationChannel;
import com.gamehok.tournament.notification.dto.NotificationDto;

import java.util.UUID;

/**
 * Notification dispatch service contract.
 */
public interface NotificationService {

    void sendNotification(Long userId, String title, String body, String type,
                          NotificationChannel channel, String referenceType, Long referenceId);

    void sendTournamentNotification(Long tournamentId, String title, String body, String type);

    PageResponse<NotificationDto> getUserNotifications(UUID userUuid, boolean unreadOnly, int page, int size);

    long countUnread(UUID userUuid);

    void markAsRead(UUID notificationUuid);

    void markAllAsRead(UUID userUuid);
}
