package org.scoula.controller.groupAccount.dto.request;

import java.util.List;

public record SettlementRequestDTO(
	String accountId,
	String accountName,
	Long amount,
	List<SettlementMembersListRequstDTO> memberList
) {
}
