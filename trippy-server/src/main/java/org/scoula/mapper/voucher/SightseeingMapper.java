package org.scoula.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.voucher.SightseeingVO;

@Mapper
public interface SightseeingMapper {
	List<SightseeingVO> findAllByUserIdOrderByViewingDateDesc(Long userId);
}
