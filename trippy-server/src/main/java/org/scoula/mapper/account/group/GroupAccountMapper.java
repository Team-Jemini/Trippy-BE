package org.scoula.mapper.account.group;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.group.GroupAccountVO;
import org.scoula.domain.account.member.AccountMemberVO;
import org.scoula.domain.notification.NotificationVO;

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

	int validateAccountIsGroupAccount(String accountId);

	int existsAccountById(String accountId);

	int checkAccountDeletionStatus(String accountId);

	GroupAccountVO getGroupAccountDetail(@Param("accountId") String accountId,
		@Param("userId") Long userId);

	void sendSettlementRequests(List<NotificationVO> notifications);

	boolean isGroupAccountUser(@Param("userId") Long userId, @Param("accountId") String accountId);

	List<GroupAccountVO> findAllByUserIdOrderByUpdatedAt(Long userId);
}
