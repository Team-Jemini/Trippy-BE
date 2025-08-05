package org.scoula.controller.identification.dto;

public record ResidentCardInquiryDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address
) {}
