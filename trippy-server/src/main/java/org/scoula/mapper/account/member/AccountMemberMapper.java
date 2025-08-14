package org.scoula.mapper.account.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.account.member.AccountMemberVO;

@Mapper
public interface AccountMemberMapper {
	List<AccountMemberVO> findAllGroupMembers(String accountId);

	boolean isGroupAccountLeader(Long userId);

	AccountMemberVO selectMemberInfo(@Param("userId") Long userId, @Param("accountId") String accountId);

	int updateTravelIdByAccountId(@Param("accountId") String accountId,
								  @Param("travelId") Long travelId);
}
