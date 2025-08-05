package org.scoula.external.codef.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.external.codef.accounts.service.CodefAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CodefTestController {

    private final CodefAccountService codefAccountService;

    @GetMapping("/test/token")
    public String testAccessToken() {
        String accessToken = codefAccountService.getAccessToken();
        log.info("컨트롤러에서 받은 AccessToken: {}", accessToken);
        return accessToken;
    }

    @GetMapping("/test/connectedId")
    public String testConnectedId() {
        String connectedId = codefAccountService.getConnectedId();
        log.info("컨트롤러에서 받은 ConnectedId: {}", connectedId);
        return connectedId;
    }

    @GetMapping("/test/accountList")
    public List<Map<String, Object>> testAccountList() throws Exception {
        String accountList = codefAccountService.getAccountList();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(accountList, Map.class);

        List<Map<String, Object>> parsedAccountList = (List<Map<String, Object>>)
                ((Map<String, Object>) responseMap.get("data")).get("resDepositTrust");

        log.info("컨트롤러에서 받은 parsedAccountList: {}", parsedAccountList);
        return parsedAccountList;
    }

}
