package org.scoula.domain.card;

import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CardVO extends BaseTime {
	private Long userId;
	private Long id;
	private String accountId;
	private String cardNumber;
	private String cardName;
	private String cardNickname;
	private Integer field; //카드 식별번호
	private Boolean isMainCard;
	private String cardImg;
}