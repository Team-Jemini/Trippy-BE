package org.scoula.controller.invite.dto.response;

import java.time.LocalDateTime;

public record AcceptInviteResponseDTO(
	String accountId,
	String accountName,
	Long userId,
	LocalDateTime AcceptedAt
) {
}
