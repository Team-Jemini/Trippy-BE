package org.scoula.controller.account.dto.request;

import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;

public record AccountRequestDTO(
        Long userId,
        String accountId,
        String accountName,
        AccountType accountType,
        Long ownerId,
        Long balance,
        String accountCurrency,
        DeletedStatus isDeleted
) {
}
