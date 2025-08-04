package org.scoula.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.user.UserVO;

@Mapper
public interface UserMapper {

	UserVO findById(Long userId);

	Boolean existsById(Long userId);
}
