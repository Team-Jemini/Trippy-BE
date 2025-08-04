package org.scoula.controller.identification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public record ResidentCardInquiryDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address
) {}
