package com.xhq.messageservice.service;

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
import com.xhq.common.utils.HostHolder;
import com.xhq.messageservice.domain.UserMessage;
import com.xhq.messageservice.mapper.UserMessageMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class dl2 {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UserMessageMapper userMessageMapper;


    public String get(String s, Long subjectId){
        Constants.apiKey="sk-5a105a62efa2412697d82a1a95d1cbf1";
        try {

            Long userId =HostHolder.getUser();
            System.out.println("iddd:"+userId);


            //redis的key: qwenUserId:#{userid}:subjectId:#{subjectId}
            List<Message> messages=new ArrayList<>();

            //redis存在该用户
            if(redisTemplate.hasKey("qwenUserId:"+userId+":subjectId:"+subjectId)){
                messages = (List<Message>) redisTemplate.opsForValue().get("qwenUserId:"+userId+":subjectId:"+subjectId);
            }else{
                //redis中没有
                //查数据库
                List<UserMessage> userMessage=userMessageMapper.getUserMessageByUserIdAndSubjectId(userId,subjectId);
                if(userMessage==null||userMessage.size()==0){
                    //数据库没有，新建一个数据list
                    Message systemMsg0 = Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
                    messages.add(systemMsg0);
                    //存到数据库
                    userMessageMapper.insert(userId,subjectId,Role.SYSTEM.getValue(),"You are a helpful assistant.");
                }else{
                    //数据库有，将数据转成Message类型，并将数据缓存到redis
                    for (UserMessage message : userMessage) {
                        Message message1=new Message();
                        BeanUtils.copyProperties(message,message1);
                        messages.add(message1);
                    }
                    //将数据缓存到redis;
                    redisTemplate.opsForValue().set("qwenUserId:"+userId+":subjectId:"+subjectId,messages);
                }
            }
            //redisTemplate.opsForValue().set("msg:1",messages);


            //将用户的问题塞进list<message>和存到数据库
            userMessageMapper.insert(userId,subjectId,Role.USER.getValue(),s);

            Message userMsg1 = Message.builder().role(Role.USER.getValue()).content(s).build();
            messages.add(userMsg1);



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
            //将系统返回的答案塞进list<message>和存到数据库
            userMessageMapper.insert(userId,subjectId,Role.ASSISTANT.getValue(),tp.toString());
            Message systemMsgtmp = Message.builder().role(Role.ASSISTANT.getValue()).content(tp.toString()).build();
            messages.add(systemMsgtmp);

            //将list<Message>存回redis
            redisTemplate.opsForValue().set("qwenUserId:"+userId+":subjectId:"+subjectId,messages);
            return tp.toString();
            //return callWithMessage(s,userId,msgManager);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.out.println(e.getMessage());
        }
        return "请求失败";
    }
}