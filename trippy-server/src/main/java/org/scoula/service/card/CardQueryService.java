package org.scoula.service.card;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.card.dto.response.CardDetailResponseDTO;
import org.scoula.controller.card.dto.response.CardSummaryResponseDTO;
import org.scoula.mapper.card.CardMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import org.scoula.domain.card.CardVO;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardQueryService {

	private final CardMapper cardMapper;

	public List<CardSummaryResponseDTO> getCardSummaries(Long userId) {
		List<CardVO> cards = cardMapper.findByUserId(userId);
		return cards.stream()
			.map(CardSummaryResponseDTO::new) // 생성자 기반 변환
			.collect(Collectors.toList());
	}

	public List<CardDetailResponseDTO> getCardDetails(Long userId) {
		List<CardVO> cards = cardMapper.findByUserId(userId);
		return cards.stream()
			.map(CardDetailResponseDTO::new) // 생성자 기반 변환
			.collect(Collectors.toList());
	}
}