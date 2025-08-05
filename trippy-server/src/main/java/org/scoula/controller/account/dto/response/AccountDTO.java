package org.scoula.controller.account.dto.response;

import org.scoula.domain.account.AccountForeign;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;

public record AccountDTO(
        Long userId,
        String accountId,
        String accountName,
        AccountType accountType,
        Long ownerId,
        Long balance,
        String accountCurrency,
        DeletedStatus isDeleted
) {}
