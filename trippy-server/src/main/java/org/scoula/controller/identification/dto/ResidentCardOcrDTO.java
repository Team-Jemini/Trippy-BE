package org.scoula.controller.identification.dto;

public record ResidentCardOcrDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address
) {}
