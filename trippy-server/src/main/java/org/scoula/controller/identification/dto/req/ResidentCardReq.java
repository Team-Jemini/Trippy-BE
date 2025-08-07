package org.scoula.controller.identification.dto.req;

public record ResidentCardReq(
        String imgUrl,
        String name,
        String identity,
        String address,
        String resIssueDate
) {}
