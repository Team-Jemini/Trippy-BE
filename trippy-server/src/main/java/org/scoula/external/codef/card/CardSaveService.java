package org.scoula.external.codef.card;

import org.scoula.domain.card.CardVO;
import org.scoula.mapper.card.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardSaveService {

	private final CardMapper cardMapper;

	@Autowired
	public CardSaveService(CardMapper cardMapper) {
		this.cardMapper = cardMapper;
	}

	public void saveCard(CardVO cardVO) {
		cardVO.setUserId(1L); // 예시
		cardVO.setAccountId("dummyAccountId"); // 예시

		cardMapper.insertCard(cardVO);
	}
}

