package org.scoula.external.codef.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ConnectedIdRequestDTOList(
        @JsonProperty("accountList")
        List<ConnectedIdRequestDTO> connectedIdRequestDTOList
) {}
