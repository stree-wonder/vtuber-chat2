package com.xhq.messageservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhq.api.dto.Subject;
import com.xhq.messageservice.domain.NewPromoptChatDTO;
import com.xhq.messageservice.domain.UserChatHistoryVO;
import com.xhq.messageservice.domain.UserMessage;


public interface UserMessageService extends IService<UserMessage> {


    UserChatHistoryVO getUserChatHistory(Subject subject);

    Long newPromoptChat(NewPromoptChatDTO newPromoptChatDTO);

    void deleteChatMessageByUserIdAndSubjectId(Subject subject);
}
