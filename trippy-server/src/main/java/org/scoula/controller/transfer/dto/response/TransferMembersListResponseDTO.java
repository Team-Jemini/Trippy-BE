package org.scoula.controller.transfer.dto.response;

public record TransferMembersListResponseDTO(
	String toAccountId,
	Long userId
) {
}
