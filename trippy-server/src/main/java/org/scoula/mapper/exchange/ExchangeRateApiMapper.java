package org.scoula.mapper.exchange;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.controller.exchange.dto.ExchangeRateDTO;

@Mapper
public interface ExchangeRateApiMapper {
    void saveDataToVO(ExchangeRateDTO exchangeRateDTO);
}
