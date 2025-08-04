package org.scoula.service.groupaccount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.member.Role;
import org.scoula.mapper.account.group.GroupAccountMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupAccountService {

	private final GroupAccountMapper mapper;

	// 모임계좌 생성
	@Transactional
	public GroupAccountCreateResponseDTO createGroupAccount(GroupAccountCreateRequestDTO request, Long userId) {
		int tryCount = 0;
		while (tryCount++ < 5) {
			String accountId = checkedCreateGroupId(userId);
			try {
				// 모임계좌 등록
				mapper.createGroupAccount(
					AccountConverter.toAccountVO(accountId, userId, AccountType.group, request));

				// 모임주 등록
				mapper.createGroupAccountMember(
					AccountConverter.toAccountMemberVO(accountId, userId, request.mainAccountId(), Role.leader));

				// 모임계좌 정보(생성날짜, 계좌번호, 계좌이름) 불러오기
				AccountVO account = mapper.selectGroupAccountById(accountId);

				return new GroupAccountCreateResponseDTO(
					account.getAccountId(),
					account.getAccountName(),
					account.getCreatedAt());

			} catch (DuplicateKeyException e) {
				log.warn("중복된 계좌번호 발생! {}", e.getMessage());
			}
		}
		throw new RuntimeException("모임계좌 생성 실패: 시도 5회 초과");
	}

	// 계좌번호 중복 없을때까지 생성
	public String checkedCreateGroupId(Long userId) {
		int tryCount = 0;
		while (tryCount++ < 5) {
			String groupId = createGroupId(userId);
			int count = mapper.existsGroupId(groupId);
			if (count == 0) {
				return groupId;
			}
		}
		throw new RuntimeException("계좌 번호 중복: 시도 5회 초과");
	}

	//모임계좌 id생성(계좌번호) 17자리
	public String createGroupId(Long userId) {
		String prePix = "0707"; //고유 번호

		// 오늘 날짜 기준 생성된 계좌 수를 카운트해서 사용
		String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
		Long todayCount = mapper.countGroupAccountsByDate(datePart);
		String sequencePart = String.format("%05d", todayCount + 1); //계산된 계좌 수 +1 하여 중복처리

		String userIdStr = String.format("%04d", userId % 10000); //userId 나눈 후 4자리로 변환
		String randomPart = String.format("%04d", ThreadLocalRandom.current().nextInt(0, 10000));

		return prePix + "-" + sequencePart + userIdStr + "-" + randomPart;
	}
}
