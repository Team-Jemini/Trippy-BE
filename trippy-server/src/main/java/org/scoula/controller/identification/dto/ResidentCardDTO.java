package org.scoula.controller.identification.dto;

public record ResidentCardDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address,
        String imgUrl,
        String QrUrl
) {}
