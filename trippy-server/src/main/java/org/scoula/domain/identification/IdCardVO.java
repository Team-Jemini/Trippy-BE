package org.scoula.domain.identification;

import lombok.*;
import org.scoula.controller.identification.dto.req.ResidentCardReq;
import org.scoula.domain.BaseTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class IdCardVO extends BaseTime {
	private Long userId;
	private String name;
	private String idCardNum;
	private String address;
	private String idCardDate;
	private String imgUrl;

	public static IdCardVO from(Long userId, ResidentCardReq req){
		return new IdCardVO(
				userId,
				req.name(),
				req.identity(),
				req.address(),
				req.resIssueDate(),
				req.imgUrl()
		);
	}
}