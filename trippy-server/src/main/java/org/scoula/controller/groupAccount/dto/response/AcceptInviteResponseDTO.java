package org.scoula.controller.groupAccount.dto.response;

import java.time.LocalDateTime;

public record AcceptInviteResponseDTO(
	String accountId,
	String accountName,
	Long userId,
	String userName,
	LocalDateTime AcceptedAt
) {
}
