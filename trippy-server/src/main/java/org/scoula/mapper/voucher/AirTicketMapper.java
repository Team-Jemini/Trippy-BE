package org.scoula.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.voucher.AirTicketVO;

@Mapper
public interface AirTicketMapper {

	List<AirTicketVO> findAllByUserIdOrderByDepartureDateDesc(Long userId);
}
