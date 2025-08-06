package org.scoula.mapper.card;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.card.CardVO;

@Mapper
public interface CardMapper {
	void insertCard(CardVO card);
}
