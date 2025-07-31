package org.scoula.mapper.identification;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.identification.PassportVO;

@Mapper
public interface PassportMapper {
	PassportVO findByUserId(Long userId);
}
