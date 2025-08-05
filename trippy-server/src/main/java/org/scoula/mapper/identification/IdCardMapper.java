package org.scoula.mapper.identification;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.identification.IdCardVO;

@Mapper
public interface IdCardMapper {
    IdCardVO getResidentCardInfo(Long userId);
}
