package org.scoula.mapper.account.group;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.member.AccountMemberVO;

@Mapper
public interface GroupAccountMapper {

	//모임계좌 생성
	int createGroupAccount(AccountVO accountVO);

	//모임계좌 생성시간 구하기
	Long countGroupAccountsByDate(String datePart);

	//계좌번호 중복 확인
	int existsGroupId(String accountId);

	//모임주 등록
	int createGroupAccountMember(AccountMemberVO member);

	//생성된 계좌 정보(생성날짜, 계좌번호, 계좌이름) 불러오기
	AccountVO selectGroupAccountById(String accountId);

	String selectUserName(Long accountId);

	int searchJoinUser(@Param("userId") Long userId, @Param("accountId") String accountId);

	void groupAccountJoin(AccountMemberVO member);
}
