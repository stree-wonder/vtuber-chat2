package com.xhq.messageservice.controller;

import com.xhq.messageservice.service.impl.SseServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController()
@RequestMapping("/api/sse")
@Api(tags = "sse连接的controller")
public class SseController {
    @Autowired
    private SseServiceImpl sseService;

    @GetMapping(value = "/listen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter listenForAnswers() {
        return sseService.connect();

    }

}
