package org.scoula.controller.groupAccount.dto.request;

public record GroupAccountCreateRequestDTO(
	String accountName,
	String email,
	String mainAccountId
) {
}