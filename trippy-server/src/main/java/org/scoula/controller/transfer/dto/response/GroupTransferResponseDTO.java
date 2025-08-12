package org.scoula.controller.transfer.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record GroupTransferResponseDTO(
	String fromAccountId,
	String fromAccountName,
	Long amount,
	Long balance,
	String currencyCode,
	LocalDateTime updatedAt,
	List<TransferMembersListResponseDTO> memberList
) {
}
