package org.scoula.service.user;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.NotFoundException;
import org.scoula.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;

	/***
	 * 유저 존재 여부 파악
	 * @param userId
	 */
	public void validateUserExists(Long userId) {
		if (!userMapper.existsById(userId)) {
			throw new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION);
		}
	}

	/***
	 * 유저 이름 받아오기
	 * @param userId
	 */
	public String geteUserName(Long userId) {
		validateUserExists(userId);
		return userMapper.findUserName(userId);
	}

}
