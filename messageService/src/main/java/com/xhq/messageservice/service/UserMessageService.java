package com.xhq.messageservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhq.api.dto.Subject;
import com.xhq.messageservice.domain.*;


public interface UserMessageService extends IService<UserMessage> {


    UserChatHistoryVO getUserChatHistory(Subject subject);

    Long newPromoptChat(NewPromoptChatDTO newPromoptChatDTO);

    void deleteChatMessageByUserIdAndSubjectId(Subject subject);

    Integer commitAnswer(AskDto dto);

    UserMessage getMessageByMessageId(String messageId);

    String generateAnswer(String s, Long subjectId);
}
