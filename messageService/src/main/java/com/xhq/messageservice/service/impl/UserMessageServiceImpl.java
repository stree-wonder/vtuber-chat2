package com.xhq.messageservice.service.impl;

import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhq.api.client.promptClient;
import com.xhq.api.dto.Prompt;
import com.xhq.api.dto.Subject;
import com.xhq.common.domain.Result;
import com.xhq.common.utils.HostHolder;
import com.xhq.messageservice.domain.NewPromoptChatDTO;
import com.xhq.messageservice.domain.UserChatHistoryVO;
import com.xhq.messageservice.domain.UserMessage;
import com.xhq.messageservice.mapper.UserMessageMapper;
import com.xhq.messageservice.service.UserMessageService;
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
    private com.xhq.api.client.subjectClient subjectClient;

    @Override
    public UserChatHistoryVO getUserChatHistory(Subject subject) {
        List<Message> messages = new ArrayList<>();

        Long userId = HostHolder.getUser();

        Long subjectId = subject.getSubjectId();

        if (redisTemplate.hasKey("qwenUserId:" + userId + ":subjectId:" + subjectId)) {//redis存在该用户的subjectId的对话
            messages = (List<Message>) redisTemplate.opsForValue().get("qwenUserId:" + userId + ":subjectId:" + subjectId);
            System.out.println("getHistorymsg:" + messages);
            UserChatHistoryVO chatHistoryVO = new UserChatHistoryVO();
            chatHistoryVO.setUserId(userId);
            chatHistoryVO.setSubjectId(subjectId);
            chatHistoryVO.setChatMessageList(messages);
            return chatHistoryVO;
        } else {
            //查数据库
            //redis中没有
            //查数据库
            List<UserMessage> userMessage = messageMapper.getUserMessageByUserIdAndSubjectId(userId, subjectId);
            if (userMessage == null || userMessage.size() == 0) {
                //数据库没有，新建一个数据list
                return null;
            } else {
                //数据库有，将数据转成Message类型，并将数据缓存到redis
                for (UserMessage message : userMessage) {
                    Message message1 = new Message();
                    BeanUtils.copyProperties(message, message1);
                    messages.add(message1);
                }
                //将数据缓存到redis;
                redisTemplate.opsForValue().set("qwenUserId:" + userId + ":subjectId:" + subjectId, messages);

                //返回数据
                UserChatHistoryVO chatHistoryVO = new UserChatHistoryVO();
                chatHistoryVO.setUserId(userId);
                chatHistoryVO.setSubjectId(subjectId);
                chatHistoryVO.setChatMessageList(messages);

                System.out.println("cahtHistory：" + chatHistoryVO);
                return chatHistoryVO;
            }
        }
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
        Result<Integer> integerResult = subjectClient.newSubject(subject);

        String s=subjectClient.hello();
        System.out.println("sss:"+s);

        Long subjectId= Long.valueOf(integerResult.getData());


        //获取prompt信息

        System.out.println("promptId:"+newPromoptChatDTO.getPromptId());
        Prompt prompt = subjectClient.getById(newPromoptChatDTO.getPromptId());


        //新建对话
        List<Message> messages = new ArrayList<>();

        //删除redis的数据
        redisTemplate.delete("qwenUserId:" + userId + ":subjectId:" + subjectId);
        //删除数据库数据
        messageMapper.deleteChatMessageByUserIdAndSubjectId(subject.getUserId(),subject.getSubjectId());

        //将初始的prompt放到数据库和redis中
        Message systemMsg0 = Message.builder().role(Role.SYSTEM.getValue()).content(prompt.getPromptText()).build();
        messages.add(systemMsg0);

        //存到数据库
        messageMapper.insert(userId, subjectId, Role.SYSTEM.getValue(), prompt.getPromptText());

        //将数据缓存到redis;
        redisTemplate.opsForValue().set("qwenUserId:" + userId + ":subjectId:" + subjectId, messages);

        return subjectId;
    }

    @Override
    public void deleteChatMessageByUserIdAndSubjectId(Subject subject) {
        Long userId=HostHolder.getUser();
        messageMapper.deleteChatMessageByUserIdAndSubjectId(userId,subject.getSubjectId());
    }

}
