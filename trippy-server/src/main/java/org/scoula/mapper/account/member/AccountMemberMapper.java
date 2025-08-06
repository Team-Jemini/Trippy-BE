package org.scoula.mapper.account.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.account.member.AccountMemberVO;

@Mapper
public interface AccountMemberMapper {
	List<AccountMemberVO> findAllGroupMembers(String accountId);

	boolean isGroupAccountLeader(Long userId);
}
