package org.scoula.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.user.UserVO;

@Mapper
public interface UserMapper {

	List<UserVO> findAll();

	UserVO findById(Long userId);

	Boolean existsById(Long userId);

	String findUserName(Long userId);

	void save(UserVO userVO);

	boolean existsLeaderInGroup(Long userId);
}
