package org.scoula.controller.account.dto.response;

import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;

public record AccountResponseDTO(
        Long userId,
        String accountId,
        String accountName,
        AccountType accountType,
        Long ownerId,
        Long balance,
        String accountCurrency,
        DeletedStatus isDeleted
) {
    public static AccountResponseDTO from(AccountVO vo, Long userId) {
        return new AccountResponseDTO(
                userId,
                vo.getAccountId(),
                vo.getAccountName(),
                vo.getAccountType(),
                userId,
                vo.getBalance(),
                vo.getAccountCurrency(),
                vo.getIsDeleted()
        );
    }
}
