package org.scoula.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@Api(tags = "Home")
public class HomeController {

    @ApiOperation(value = "게시글 목록", notes = "게시글 목록을 얻는 API")
    @GetMapping("/")
    public String helloTrippy() {
        return "Hello from Trippy!";
    }

    @GetMapping("/v2")
    public String redirectToSwaggerUI() {
        return "redirect:/swagger-ui.html";
    }

}
