package org.scoula.mapper.card;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.card.CardVO;

import org.apache.ibatis.annotations.Param;
import org.scoula.controller.card.dto.response.CardSummaryResponseDTO;
import org.scoula.controller.card.dto.response.CardDetailResponseDTO;



@Mapper
public interface CardMapper {
	void insertCard(CardVO card);
	List<CardVO> findByUserId(Long userId);
	int deleteCardById(Long cardId);
}
