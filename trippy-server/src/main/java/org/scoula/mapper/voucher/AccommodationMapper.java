package org.scoula.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.voucher.AccommodationVO;

@Mapper
public interface AccommodationMapper {
	List<AccommodationVO> findAllByUserIdOrderByCheckOutDesc(Long userId);
}
