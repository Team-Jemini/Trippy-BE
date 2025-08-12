package org.scoula.controller.travel.log.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Builder
@ApiModel(description = "Travel Log 생성 요청 DTO")
public record TravelLogCreateDTO (

        @ApiModelProperty(value = "연결된 계좌 ID", required = true, example = "acct_20250729_001")
        @NotBlank(message = "accountId는 필수입니다.")
        @Size(max = 50, message = "accountId는 최대 50자입니다.")
        String accountId,

        @ApiModelProperty(value = "여행 제목", required = true, example = "오사카 3박 4일 먹투어")
        @NotBlank(message = "title은 필수입니다.")
        @Size(max = 100, message = "title은 최대 100자입니다.")
        String title,

        @ApiModelProperty(value = "여행 시작일시 (ISO-8601)", required = true, example = "2025-07-10T09:00:00")
        @NotNull(message = "travelBeginDate는 필수입니다.")
        LocalDateTime travelBeginDate,

        @ApiModelProperty(value = "여행 종료일시 (ISO-8601)", required = true, example = "2025-07-13T18:00:00")
        @NotNull(message = "travelEndDate는 필수입니다.")
        LocalDateTime travelEndDate,

        @ApiModelProperty(value = "여행지(도시/지역명)", required = true, example = "오사카, 일본")
        @NotBlank(message = "destination은 필수입니다.")
        @Size(max = 50, message = "destination은 최대 50자입니다.")
        String destination,

        @ApiModelProperty(value = "AI 생성 로그 여부", example = "false")
        Boolean isGenerated,

        @ApiModelProperty(value = "대표 이미지 URL", example = "https://example.com/travel/abc.jpg")
        @Size(max = 255, message = "travelImg URL은 최대 255자입니다.")
        @Pattern(regexp = "^https?://.+", message = "travelImg는 http(s) URL이어야 합니다.")
        String travelImg
) {
    // 교차 필드 검증: 종료일이 시작일 이전이면 안 됨
    @AssertTrue(message = "travelEndDate는 travelBeginDate 이후여야 합니다.")
    public boolean isEndAfterBegin() {
        if (travelBeginDate == null || travelEndDate == null) return true; // 다른 @NotNull이 처리
        return !travelEndDate.isBefore(travelBeginDate);
    }
}