package com.xhq.messageservice.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    public SseEmitter connect() ;
}
