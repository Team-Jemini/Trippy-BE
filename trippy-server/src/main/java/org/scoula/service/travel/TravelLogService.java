package org.scoula.service.travel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.BadRequestException;
import org.scoula.common.exception.model.TrippyException;
import org.scoula.controller.travel.log.dto.req.TravelLogCreateDTO;
import org.scoula.controller.travel.log.dto.req.TravelLogTransactionDTO;
import org.scoula.controller.travel.log.dto.req.TravelLogTransactionListDTO;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.domain.transaction.TransactionVO;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.external.s3.S3Service;
import org.scoula.mapper.account.member.AccountMemberMapper;
import org.scoula.mapper.travel.TravelLogMapper;
import org.scoula.service.transaction.TransactionService;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TravelLogService {
	private final TravelLogMapper travelLogMapper;
	private final UserService userService;
	private final S3Service s3Service;
	private final TransactionService transactionService;
	private final AccountMemberMapper accountMemberMapper;

	public List<TravelLogDTO> getTravelLogs(final Long userId) {
		userService.validateUserExists(userId);

		List<Map<String, Object>> logs = travelLogMapper.getAllTravelLogs(userId);

		return logs.stream()
			.map(log -> new TravelLogDTO(
				((Number)log.get("travelId")).longValue(),
				((Number)log.get("userId")).longValue(),
				((String)log.get("accountId")),
				(String)log.get("title"),
				(LocalDateTime)log.get("travelBeginDate"),
				(LocalDateTime)log.get("travelEndDate"),
				(String)log.get("destination"),
				(Boolean)log.get("isGenerated"),
				(String)log.get("travelImg"),
				((Number)log.get("memberCount")).longValue()
			))
			.toList();
	}

	@Transactional
	public void createTravelLog(final Long userId, final TravelLogCreateDTO dto, final MultipartFile travelImg) {

		userService.validateUserExists(userId);
		String uploadedUrl = s3Service.uploadTravelLogImage(travelImg);

		TravelLogVO travelLog = TravelLogVO.builder()
			.userId(userId)
			.accountId(dto.accountId())
			.title(dto.title())
			.travelBeginDate(dto.travelBeginDate())
			.travelEndDate(dto.travelEndDate())
			.destination(dto.destination())
			.isGenerated(dto.isGenerated())
			.travelImg(uploadedUrl) // S3 URL 저장
			.build();

		travelLogMapper.save(travelLog);
		Long travelId = travelLogMapper.selectLastInsertId();
		accountMemberMapper.updateTravelIdByAccountId(dto.accountId(), travelId);
	}

	/**
	 * 여행 기간이 기존 로그와 겹치지 않으면 true, 겹치면 false 반환 (경계 포함 겹침)
	 */
	public boolean isTravelDateAvailable(final Long userId,
										 final LocalDateTime begin,
										 final LocalDateTime end) {
		userService.validateUserExists(userId);

		if (begin == null || end == null) {
			throw new BadRequestException(ErrorCode.TRAVEL_DATE_REQUIRED);
		}
		if (end.isBefore(begin)) {
			throw new BadRequestException(ErrorCode.INVALID_TRAVEL_DATE);
		}

		// 겹치지 않는 조건: (existing_end < begin) OR (existing_begin > end)
		// 따라서 겹치는 것의 count: NOT (existing_end < begin OR existing_begin > end)
		int overlapCount = travelLogMapper.countOverlappingTravelLogs(userId, begin, end);
		return overlapCount == 0;
	}

	/***
	 * 여행 기간동안의 거래 내역 조회
	 * 1. 여행 기간동안의 거래 내역 전체 조회
	 * 2. 전체 여행 기간동안의  지출 총 합계와, 오늘의 거래 일자로 데이터 조회
	 * @param userId
	 * @param travelId
	 * @return TravelLogTransactionListDTO
	 */
	public TravelLogTransactionListDTO getTravelTransactions(final Long userId, final Long travelId) {

		TravelLogVO travelLog = travelLogMapper.findByTravelId(travelId);
		List<TransactionVO> transactions = transactionService.getUserExpenseTransactions(userId,
			travelLog.getAccountId(), travelLog.getTravelBeginDate(), travelLog.getTravelEndDate());

		Long totalAmount = calculateTotalAmount(transactions);
		Long todayAmount = calculateTodayAmount(transactions);

		return new TravelLogTransactionListDTO(
			travelId,
			todayAmount,
			totalAmount,
			transactions.stream().map(TravelLogTransactionDTO::from).toList());
	}

	private Long calculateTotalAmount(List<TransactionVO> transactions) {
		return transactions.stream().mapToLong(TransactionVO::getAmount).sum();
	}

	private Long calculateTodayAmount(List<TransactionVO> transactions) {
		LocalDate today = LocalDate.now();
		return transactions.stream()
			.filter(t -> t.getCreatedAt().toLocalDate().equals(today))
			.mapToLong(TransactionVO::getAmount)
			.sum();
	}

	/***
	 * 여행 기간동안의 거래 내역 상세 조회 ( 단건 조회 )
	 * @param transactionId
	 * @return
	 */
	public TravelLogTransactionDTO getTravelLogDetailTransaction(final Long transactionId) {
		return TravelLogTransactionDTO.from(transactionService.getTransaction(transactionId));
	}



}
