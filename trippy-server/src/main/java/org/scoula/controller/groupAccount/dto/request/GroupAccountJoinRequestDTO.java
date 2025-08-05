package org.scoula.controller.groupAccount.dto.request;

public record GroupAccountJoinRequestDTO(
	String token,
	String mainAccountId
) {
}
