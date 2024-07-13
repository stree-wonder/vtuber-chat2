package com.xhq.messageservice.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhq.api.client.userClient;
import com.xhq.api.dto.Prompt;
import com.xhq.api.dto.Subject;
import com.xhq.common.Constant.Constant;
import com.xhq.common.domain.Result;
import com.xhq.common.domain.User;
import com.xhq.common.utils.HostHolder;
import com.xhq.messageservice.domain.*;
import com.xhq.messageservice.mapper.UserMessageMapper;
import com.xhq.messageservice.service.UserMessageService;
import com.xhq.mq.MyMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    @Autowired
    private  RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UserMessageMapper messageMapper;


    @Autowired
    private userClient userClient;

    @Autowired
    private MyMessageProducer messageProducer;

    @Override
    public UserChatHistoryVO getUserChatHistory(Subject subject) {
        List<UserMessage> usermessages = new ArrayList<>();
        List<Message> messages=new ArrayList<>();

        Long userId = HostHolder.getUser();

        Long subjectId = subject.getSubjectId();

        if (redisTemplate.hasKey("qwenUserId:" + userId + ":subjectId:" + subjectId)) {
            //redis存在该用户的subjectId的对话
            usermessages = (List<UserMessage>) redisTemplate.opsForValue().get("qwenUserId:" + userId + ":subjectId:" + subjectId);
        } else {
            //查数据库,redis中没有
            usermessages = messageMapper.getUserMessageByUserIdAndSubjectId(userId, subjectId);
            if (usermessages == null || usermessages.size() == 0) {
                //数据库没有，
                return null;
            } else {
                //将数据缓存到redis;
                redisTemplate.opsForValue().set("qwenUserId:" + userId + ":subjectId:" + subjectId, usermessages);
            }
        }
        //将数据转成Message类型，返回
        for (UserMessage message : usermessages) {
            Message message1 = new Message();
            BeanUtils.copyProperties(message, message1);
            messages.add(message1);
        }

        //返回数据
        UserChatHistoryVO chatHistoryVO = new UserChatHistoryVO();
        chatHistoryVO.setUserId(userId);
        chatHistoryVO.setSubjectId(subjectId);
        chatHistoryVO.setChatMessageList(messages);

        System.out.println("cahtHistory：" + chatHistoryVO);
        return chatHistoryVO;
    }


    @Override
    public Long newPromoptChat(NewPromoptChatDTO newPromoptChatDTO) {

        Long userId = HostHolder.getUser();

        //新建project
        Subject subject = new Subject();
        subject.setSubject(newPromoptChatDTO.getSubject());
        subject.setUserId(userId);

        //TODO subjectmapper
        System.out.println("subject:"+subject);
        Result<Integer> integerResult = userClient.newSubject(subject);

        String s= userClient.hello();
        System.out.println("sss:"+s);

        Long subjectId= Long.valueOf(integerResult.getData());


        //获取prompt信息

        System.out.println("promptId:"+newPromoptChatDTO.getPromptId());
        Prompt prompt = userClient.getById(newPromoptChatDTO.getPromptId());


        //新建对话
        List<UserMessage> userMessages = new ArrayList<>();

        //删除redis的数据
        redisTemplate.delete("qwenUserId:" + userId + ":subjectId:" + subjectId);
        //删除数据库数据
        messageMapper.deleteChatMessageByUserIdAndSubjectId(subject.getUserId(),subject.getSubjectId());

        //将初始的prompt放到数据库和redis中

        //Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content(prompt.getPromptText()).build();
        UserMessage userMessage = UserMessage.builder()
                .userId(userId)
                .subjectId(subjectId)
                .role(Constant.QWEN_MESSAGE_ROLE_SYSTEM)
                .content(prompt.getPromptText())
                .status(Constant.QUESTION_STATUS_SUCCEED)
                .build();

        userMessages.add(userMessage);

        //存到数据库
        messageMapper.insert(userMessage);

        //将数据缓存到redis;
        redisTemplate.opsForValue().set("qwenUserId:" + userId + ":subjectId:" + subjectId, userMessages);

        return subjectId;
    }

    @Override
    public void deleteChatMessageByUserIdAndSubjectId(Subject subject) {
        Long userId=HostHolder.getUser();
        messageMapper.deleteChatMessageByUserIdAndSubjectId(userId,subject.getSubjectId());
    }

    //回答问题
    @Override
    public Integer commitAnswer(AskDto dto) {
        Long userId=HostHolder.getUser();
        //查询是当前用户否有正在生成的问题
        UserMessage lastmessage=messageMapper.selectLastQuestion(userId);
        if(lastmessage!=null){
            //存在未完全生成的问题，拒绝再提问
            return 401;
        }
        //将问题存到数据库
        UserMessage message= UserMessage.builder()
                .userId(userId)
                .subjectId(dto.getSubjectId())
                .role(Constant.QWEN_MESSAGE_ROLE_USER)
                .content(dto.getQuestion())
                .status(Constant.QUESTION_STATUS_WAIT)
                .build();
        messageMapper.insert(message);
        //将问题的uuid发送给消息队列
        String questionId=message.getId();
        if(questionId==null){
            //问题插入失败
            return 401;
        }
        messageProducer.sendMessage(questionId);
        return 200;
    }

    @Override
    public UserMessage getMessageByMessageId(String messageId) {
        return messageMapper.getUserMessageByMessageId(messageId);
    }

    @Override
    public String generateAnswer(String s, Long subjectId){
        Constants.apiKey="sk-5a105a62efa2412697d82a1a95d1cbf1";
        try {

            Long userId =HostHolder.getUser();
            System.out.println("iddd:"+userId);

            //redis的key: qwenUserId:#{userid}:subjectId:#{subjectId}
            List<UserMessage> userMessages=new ArrayList<>();

            //redis存在该用户
            if(redisTemplate.hasKey("qwenUserId:"+userId+":subjectId:"+subjectId)){
                userMessages = (List<UserMessage>) redisTemplate.opsForValue().get("qwenUserId:"+userId+":subjectId:"+subjectId);
            }else{
                //redis中没有
                //查数据库
                 userMessages=messageMapper.getUserMessageByUserIdAndSubjectId(userId,subjectId);
                if(userMessages==null||userMessages.size()==0){
                    //数据库没有，新建一个数据list
                    //Message systemMsg0 = Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
                    UserMessage userMessage= UserMessage.builder()
                            .userId(userId)
                            .subjectId(subjectId)
                            .role(Constant.QWEN_MESSAGE_ROLE_SYSTEM)
                            .content("You are a helpful assistant.")
                            .status(Constant.QUESTION_STATUS_SUCCEED)
                            .build();
                    userMessages.add(userMessage);
                    //存到数据库
                    messageMapper.insert(userMessage);
                }else{
                    //数据库有，将数据缓存到redis
                    redisTemplate.opsForValue().set("qwenUserId:"+userId+":subjectId:"+subjectId,userMessages);
                }
            }


            //构造问题，向api提问
            List<Message> messages=new ArrayList<>();
                 for (UserMessage message : userMessages) {
                     Message message1=new Message();
                     BeanUtils.copyProperties(message,message1);
                     messages.add(message1);
                 }


            Generation gen = new Generation();
            QwenParam params = QwenParam.builder().model("qwen-plus")
                    .messages(messages)
                    .seed(1234)
                    .topP(0.8)
                    .resultFormat("message")
                    .maxTokens(1500)
                    .temperature((float)0.85)
                    .repetitionPenalty((float)1.0)
                    .build();
            System.out.println("555");
            GenerationResult result = gen.call(params);
            System.out.println("666");
            List<GenerationOutput.Choice> tmp= result.getOutput().getChoices();
            StringBuilder tp= new StringBuilder();
            for (int i = 0; i < tmp.size(); i++) {
                System.out.println(tmp.get(i).getMessage().getContent());
                tp.append(tmp.get(i).getMessage().getContent());
            }

            //将系统返回的答案创建UserMessage,修改问题的状态为succeed
            UserMessage userMessage= UserMessage.builder()
                    .userId(userId)
                    .subjectId(subjectId)
                    .role(Constant.QWEN_MESSAGE_ROLE_ASSISTANT)
                    .content(tp.toString())
                    .status(Constant.QUESTION_STATUS_SUCCEED)
                    .build();

            messageMapper.insert(userMessage);

            messageMapper.insert(userId,subjectId,Role.ASSISTANT.getValue(),tp.toString());
            Message systemMsgtmp = Message.builder().role(Role.ASSISTANT.getValue()).content(tp.toString()).build();
            //TODO
            //userMessages.add(systemMsgtmp);

            //将list<Message>存回redis
            redisTemplate.opsForValue().set("qwenUserId:"+userId+":subjectId:"+subjectId,userMessages);
            return tp.toString();
            //return callWithMessage(s,userId,msgManager);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.out.println(e.getMessage());
        }
        return "请求失败";
    }

}
