package org.scoula.service.voucher;

import java.util.List;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.NotFoundException;
import org.scoula.controller.voucher.dto.request.SightSeeingDto;
import org.scoula.controller.voucher.dto.response.AccommodationDetailDto;
import org.scoula.controller.voucher.dto.response.AccommodationInfo;
import org.scoula.controller.voucher.dto.response.SightSeeingInfo;
import org.scoula.controller.voucher.dto.response.VoucherDto;
import org.scoula.domain.voucher.AccommodationVO;
import org.scoula.domain.voucher.SightseeingVO;
import org.scoula.external.s3.S3Service;
import org.scoula.mapper.voucher.AccommodationMapper;
import org.scoula.mapper.voucher.SightseeingMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoucherService {

	private final AccommodationMapper accommodationMapper;
	private final SightseeingMapper sightseeingMapper;
	private final UserService userService;
	private final S3Service s3Service;

	/***
	 * 바우처 전체 조회
	 * 1. 호텔은  체크아웃이 가장 늦은거 기준으로 정렬 ( LocalDate 를 25.06.01(토) 15:00 로 포매팅)
	 * 2. 관광도 예약날짜 최신순으로 정렬 (25.06.01(토) 15:00로 포매팅)
	 * @param userId
	 * @return VoucherDto -> List<AccommodationInfo>, List<SightSeeingInfo></SightSeeingInfo>
	 */
	public VoucherDto getVouchers(final Long userId) {
		userService.validateUserExists(userId);

		//1. 호텔 데이터 불러오기
		List<AccommodationVO> accommodations = accommodationMapper.findAllByUserIdOrderByCheckOutDesc(userId);
		List<AccommodationInfo> accommodationInfos = accommodations.stream()
			.map(AccommodationInfo::from)
			.toList();

		List<SightseeingVO> sightseeingList = sightseeingMapper.findAllByUserIdOrderByViewingDateDesc(userId);
		List<SightSeeingInfo> sightseeingInfos = sightseeingList.stream()
			.map(SightSeeingInfo::from)
			.toList();

		return new VoucherDto(accommodationInfos, sightseeingInfos);
	}

	/***
	 * 숙소 예약 상세 조회
	 * @param userId
	 * @param accommodationId
	 * @return AccommodationDetailDto
	 */
	public AccommodationDetailDto getDetailAccommodation(final Long userId, final String accommodationId) {
		AccommodationVO accommodationVO = accommodationMapper.findById(accommodationId);
		if (accommodationVO == null) {
			throw new NotFoundException(ErrorCode.ACCOMMODATION_NOT_FOUND_EXCEPTION);
		}

		return AccommodationDetailDto.from(accommodationVO, userService.getUserName(userId));
	}

	/***
	 * 관광 예약 바우처 생성
	 * @param userId
	 * @param sightSeeingDto
	 * @param sightSeeingVoucherImg
	 */
	@Transactional
	public void createSightseeing(final Long userId, final SightSeeingDto sightSeeingDto, final MultipartFile sightSeeingVoucherImg) {
		userService.validateUserExists(userId);

		String uploadedUrl = s3Service.uploadSightSeeingImage(sightSeeingVoucherImg);

		SightseeingVO sightseeingVO = SightseeingVO.builder()
			.userId(userId)
			.name(sightSeeingDto.name())
			.viewingDate(sightSeeingDto.viewingDate())
			.voucherImg(uploadedUrl)
			.build();

		sightseeingMapper.save(sightseeingVO);
	}
}
