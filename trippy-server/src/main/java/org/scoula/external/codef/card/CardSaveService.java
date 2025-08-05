package org.scoula.external.codef.card;

import org.scoula.domain.card.CardVO;
import org.scoula.mapper.card.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.scoula.domain.card.CardVO;
import org.scoula.mapper.card.CardMapper;

@Service
@RequiredArgsConstructor
public class CardSaveService {

	private final CardMapper cardMapper;

	public void saveCard(CardVO cardVO) {
		cardVO.setUserId(1L); // 예시
		cardVO.setAccountId("dummyAccountId"); // 예시

		cardMapper.insertCard(cardVO);
	}
}

