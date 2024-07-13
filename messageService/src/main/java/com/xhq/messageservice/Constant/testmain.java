package com.xhq.messageservice.Constant;

import com.xhq.common.Constant.Constant;
import com.xhq.messageservice.domain.UserMessage;
import com.xhq.messageservice.mapper.UserMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class testmain {

    @Autowired
    static
    UserMessageMapper userMessageMapper;


    public static void main(String[] args) {

        UserMessage userMessage= UserMessage.builder()
                .userId(1L)
                .subjectId(1L)
                .role(Constant.QWEN_MESSAGE_ROLE_ASSISTANT)
                .content("测试插入")
                .status(Constant.QUESTION_STATUS_SUCCEED)
                .build();
        userMessageMapper.insert(userMessage);
        System.out.println("插入完成");
    }
}
