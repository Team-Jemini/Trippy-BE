package org.scoula.controller.transfer.dto.response;

public record TransferMembersListResponseDTO(
	String mainAccountId,
	Long userId,
	String userName) {
}
