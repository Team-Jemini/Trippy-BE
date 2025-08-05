package org.scoula.service.card;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.card.dto.response.CardDetailResponseDTO;
import org.scoula.controller.card.dto.response.CardSummaryResponseDTO;
import org.scoula.mapper.card.CardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardQueryService {

	private final CardMapper cardMapper;

	public List<CardSummaryResponseDTO> getCardSummaries(Long userId) {
		return cardMapper.getCardSummaries(userId);
	}

	public List<CardDetailResponseDTO> getCardDetails(Long userId) {
		return cardMapper.getCardDetails(userId);
	}
}
