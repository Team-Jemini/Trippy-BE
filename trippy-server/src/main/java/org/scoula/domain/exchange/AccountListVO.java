package org.scoula.domain.exchange;

import lombok.*;
import org.scoula.domain.BaseTime;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.DeletedStatus;
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AccountListVO extends BaseTime {
    private Long userId;
    private String accountId;
    private String accountName;
    private AccountType accountType;
    private Long ownerId;
    private Long balance;
    private String accountCurrency;
    private DeletedStatus isDeleted;
}
