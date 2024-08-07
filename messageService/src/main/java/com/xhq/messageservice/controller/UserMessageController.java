package com.xhq.messageservice.controller;


import com.xhq.common.domain.Result;
import com.xhq.messageservice.domain.*;
import com.xhq.messageservice.service.UserMessageService;
import com.xhq.messageservice.service.dl2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController()
@RequestMapping("/api/userMessage")
@Api(tags = "用户发送问题有关的的controller")
public class UserMessageController {

    @Autowired
    private dl2 dl;


    @Autowired
    private UserMessageService userMessageService;



    @PostMapping("/deleteChatMessageByUserIdAndSubjectId")
    @ApiOperation("根据userId和subjectId删除聊天记录")
    public void deleteChatMessageByUserIdAndSubjectId(@RequestBody Subject subject){
        com.xhq.api.dto.Subject subject1=new com.xhq.api.dto.Subject();
        BeanUtils.copyProperties(subject,subject1);
        userMessageService.deleteChatMessageByUserIdAndSubjectId(subject1);
    }

    /**
     * 用户发起问题，并携带历史聊天主题的id
     * @param dto
     * @return
     */
    @PostMapping("/askQuestion2")
    @ApiOperation("用户询问")
    public Result<String> userAskQuestion2(@RequestBody AskDto dto){

        String ans= dl.get(dto.getQuestion(),dto.getSubjectId());
        return Result.success(ans);
    }

    /**
     * 用户发起问题，并携带历史聊天主题的id,异步消息队列回答
     * @param dto
     * @return
     */
    @PostMapping("/askQuestion3")
    @ApiOperation("用户询问")
    public Result userAskQuestion3(@RequestBody AskDto dto){
        if(dto.getSubjectId()==null||dto.getQuestion()==null){
            return Result.error("提问为空");
        }
        Integer status=userMessageService.commitAnswer(dto);
        return Result.success();
    }



    @PostMapping("/getUserChatHistory")
    @ApiOperation("得到用户某个对话的历史记录返回前端")
    public Result<UserChatHistoryVO> getUserChatHistory(@RequestBody Subject subject){

        com.xhq.api.dto.Subject subject1=new com.xhq.api.dto.Subject();
        BeanUtils.copyProperties(subject,subject1);

        UserChatHistoryVO  userChatHistory=userMessageService.getUserChatHistory(subject1);
        if(userChatHistory==null){
            return Result.error("没有该用户该subjectId的对话");
        }
        return Result.success(userChatHistory);
    }


    @PostMapping("/newPromptChat")
    @ApiOperation("新建prompt对话")
    public Result<Long> newPromoptChat(@RequestBody NewPromoptChatDTO newPromoptChatDTO){
        Long subjectId=userMessageService.newPromoptChat(newPromoptChatDTO);
        System.out.println("new prompt："+newPromoptChatDTO);
        return Result.success(subjectId);
    }


}
