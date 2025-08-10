package org.scoula.controller.groupAccount.dto.response;

import java.time.LocalDateTime;

public record GroupAccountJoinedResponseDTO(
	String accountId,
	String accountName,
	LocalDateTime createdAt
) {
}
