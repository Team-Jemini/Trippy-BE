package org.scoula.domain.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.scoula.domain.BaseTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ForeignAccountBalanceVO extends BaseTime {
<<<<<<< HEAD
    private Long balanceId;
    private String userId;
    private String accountId;
    private String currencyCode;
    private Double balance;

=======
	private String userId;
	private String accountId;
	private String currencyCode;
	private Double balance;
>>>>>>> cc60e989b5326a1a10f630d7a3ef5285c6071263
}
