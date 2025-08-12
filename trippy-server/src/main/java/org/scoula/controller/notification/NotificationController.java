package org.scoula.controller.notification;

import java.util.List;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.config.resolver.UserId;
import org.scoula.controller.notification.request.NotiDTO;
import org.scoula.service.notification.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Notification", description = "알람 내역을 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/noti")
public class NotificationController {

	private final NotificationService notificationService;

	@ApiOperation(value = "[JWT] 알림 전제 조회", notes = "알림을 전체 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "알림전체 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@GetMapping()
	public SuccessResponse<List<NotiDTO>> getNoti(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_ALL_NOTIS_SUCCESS, notificationService.getNotis(userId));
	}

	@ApiOperation(value = "[JWT] 알림 상세 단건 조회", notes = "알림을 상세 단건 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "알림 상세 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@GetMapping("/{notiId}")
	public SuccessResponse<NotiDTO> getDetailNoti(@ApiIgnore @UserId Long userId,
		@ApiParam(value = "알림 단건Id", required = true, example = "1") @PathVariable Long notiId) {
		return SuccessResponse.success(SuccessCode.GET_DETAIL_NOTI_SUCCESS,
			notificationService.getDetailNoti(notiId));
	}
}
