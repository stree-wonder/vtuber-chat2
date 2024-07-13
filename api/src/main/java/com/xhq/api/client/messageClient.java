package com.xhq.api.client;

import com.xhq.api.config.DefaultFeignConfig;
import com.xhq.api.dto.AskDto;
import com.xhq.api.dto.NewPromoptChatDTO;
import com.xhq.api.dto.Subject;
import com.xhq.api.dto.UserChatHistoryVO;
import com.xhq.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="messageService",configuration = DefaultFeignConfig.class)
public interface messageClient {
    /**
     * 用户发起问题，并携带历史聊天主题的id
     * @param dto
     * @return
     */
    @PostMapping("/api/userMessage/askQuestion2")
    public Result<String> userAskQuestion2(@RequestBody AskDto dto);


    @PostMapping("/api/userMessage/getUserChatHistory")
    public Result<UserChatHistoryVO> getUserChatHistory(@RequestBody Subject subject);


    @PostMapping("/api/userMessage/newPromptChat")
    public Result<Integer> newPromoptChat(@RequestBody NewPromoptChatDTO newPromoptChatDTO);

    @PostMapping("/api/userMessage/deleteChatMessageByUserIdAndSubjectId")
    public void deleteChatMessageByUserIdAndSubjectId(@RequestBody Subject subject);
}
