package org.scoula.controller.voucher;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.voucher.dto.request.SightSeeingDto;
import org.scoula.controller.voucher.dto.response.AccommodationDetailDto;
import org.scoula.controller.voucher.dto.response.VoucherDto;
import org.scoula.service.voucher.VoucherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Voucher")
@RestController
@RequiredArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {

	private final VoucherService voucherService;

	@ApiOperation(value = "[JWT] 바우처(숙소,관광) 전제 조회", notes = "바우처(숙소,관광) 전체 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "바우처(숙소,관광) 전체조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping()
	public SuccessResponse<VoucherDto> getAirTicket(@RequestParam Long userId) {
		return SuccessResponse.success(SuccessCode.FIND_VOUCHER_SUCCESS, voucherService.getVouchers(userId));
	}

	@ApiOperation(value = "[JWT] 숙소예약 상세 조회", notes = "숙소예약 상세 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "숙소예약 상세 조회 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/accommodation/{accommodationId}")
	public SuccessResponse<AccommodationDetailDto> getAirTicket(@RequestParam Long userId,
		@ApiParam(value = "숙소 예약 번호 (ex. 1616070384)", example = "1616070384") @PathVariable String accommodationId) {
		return SuccessResponse.success(SuccessCode.FIND_DETAIL_ACCOMMODATION_SUCCESS,
			voucherService.getDetailAccommodation(userId, accommodationId));
	}

	@ApiOperation(value = "[JWT] 관광예약 바우처 업로드", notes = "관광예약 바우처 업로드 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "관광예약 생성 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "관광바우처 정보가 잘못되었습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@PostMapping(value = "/sightseeing", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public SuccessNonDataResponse postSightseeing(
		@RequestParam Long userId,
		@RequestPart(value = "관광 바우처 예약 이미지", required = false) MultipartFile sightSeeingVoucherImg,
		@ApiParam(value = "관광 바우처 JSON (예: {\"name\":\"그랜드캐니언\", \"viewingDate\":\"2025-09-01T14:00\"})") @RequestPart SightSeeingDto sightSeeingDto) {
		voucherService.createSightseeing(userId, sightSeeingDto, sightSeeingVoucherImg);
		return SuccessNonDataResponse.success(SuccessCode.CREATE_SIGHTSEEING_SUCCESS);
	}

}
