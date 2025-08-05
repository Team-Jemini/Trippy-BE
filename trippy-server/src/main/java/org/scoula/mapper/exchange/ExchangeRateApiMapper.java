package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.controller.exchange.dto.ExchangeRateDTO;
import org.scoula.domain.exchange.ExchangeRateVO;

@Mapper
public interface ExchangeRateApiMapper {
    void save(ExchangeRateVO exchangeRateVO);
}
