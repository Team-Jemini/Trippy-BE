package org.scoula.mapper.user;

import org.apache.ibatis.annotations.Select;
import org.scoula.domain.user.UserVO;

public interface UserMapper {

	// 예시니까 이거 처음 쓰시는 분이 삭제하세여~
	@Select("SELECT sysdate()")
	String getTime();

	// 예시니까 이거 처음 쓰시는 분이 삭제하세여~
	UserVO findById(Long id);
}
