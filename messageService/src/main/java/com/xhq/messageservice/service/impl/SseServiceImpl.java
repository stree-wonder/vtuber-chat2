package com.xhq.messageservice.service.impl;

import com.xhq.common.utils.HostHolder;
import com.xhq.messageservice.domain.AnswerVO;
import com.xhq.messageservice.domain.UserMessage;
import com.xhq.messageservice.service.SseService;
import com.xhq.messageservice.service.UserMessageService;
import com.xhq.mq.MyMessageConsumer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseServiceImpl implements SseService {

    @Autowired
    private MyMessageConsumer messageConsumer;

    @Autowired
    private UserMessageService userMessageService;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); // 保存SSE连接

    public SseEmitter connect() {
        Long id = HostHolder.getUser();
        String userId=String.valueOf(id);

        SseEmitter emitter = new SseEmitter(0L); // 0L表示超时时间无限长
        emitters.put(userId, emitter); // 保存连接
        // 设置超时处理器，防止连接无故关闭
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        // 假设这里有个方法可以注册一个监听器，当有新答案时调用notifyAnswer
        // 实际逻辑需与消息队列或其他处理机制相结合

        // 发送一个初始化事件，确保连接成功
        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected"));
        } catch (IOException e) {
            // 处理错误
            System.out.println("sse连接异常 userId："+userId);
        }

        return emitter;
    }

    // 假设的逻辑，从某个地方获取到答案并推送给对应subjectId的客户端
    private void notifyAnswer(String userId, String messageId) {
        SseEmitter emitter = emitters.get(userId);
        UserMessage userMessage=userMessageService.getMessageByMessageId(messageId);
        if(userMessage==null){
            throw new RuntimeException("api生成消息不存在,messageId:"+messageId);
        }
        AnswerVO answer=new AnswerVO();
        answer.setUserId(userId);
        answer.setSubjectId(userMessage.getSubjectId());
        answer.setAnswer(userMessage.getContent());

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("answer") // 事件名，前端可监听此事件
                        .data(answer)); // 发送答案数据
            } catch (IOException e) {
                emitters.remove(userId); // 移除异常的连接
            }
        }
    }
}
