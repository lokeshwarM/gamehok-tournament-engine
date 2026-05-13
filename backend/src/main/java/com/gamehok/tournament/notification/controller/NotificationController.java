package com.gamehok.tournament.notification.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.notification.dto.NotificationDto;
import com.gamehok.tournament.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for user notifications.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "User notification management")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userUuid}")
    @Operation(summary = "Get user notifications")
    public ResponseEntity<ApiResponse<PageResponse<NotificationDto>>> getNotifications(
            @PathVariable UUID userUuid,
            @RequestParam(defaultValue = "false") boolean unreadOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserNotifications(userUuid, unreadOnly, page, size)));
    }

    @GetMapping("/user/{userUuid}/unread-count")
    @Operation(summary = "Get unread notification count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable UUID userUuid) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.countUnread(userUuid)));
    }

    @PatchMapping("/{uuid}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID uuid) {
        notificationService.markAsRead(uuid);
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }

    @PatchMapping("/user/{userUuid}/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@PathVariable UUID userUuid) {
        notificationService.markAllAsRead(userUuid);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
}
