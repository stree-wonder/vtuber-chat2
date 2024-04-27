package com.xhq.subject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhq.common.utils.HostHolder;
import com.xhq.subject.domain.Subject;
import com.xhq.subject.mapper.SubjectMapper;
import com.xhq.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mytx
* @description 针对表【subject】的数据库操作Service实现
* @createDate 2024-03-18 21:09:20
*/
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
    implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private  com.xhq.api.client.messageClient messageClient;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<Subject> getUserSubjectList() {

        //System.out.println("user:::"+user);
        List<Subject> subjectList=subjectMapper.getUserSubjectList(HostHolder.getUser());
        return subjectList;
    }


    @Override
    public Long newSubject(Subject subject) {

        subject.setUserId(HostHolder.getUser());
        System.out.println("newsubject用户id：：："+HostHolder.getUser());
        subjectMapper.newSubject(subject);
        return subject.getSubjectId();
    }

    @Override
    public void deleteChatHistoryBySubjectId(Long subjectId) {
        Long userId=HostHolder.getUser();

        //删除subject
        subjectMapper.deleteByUserIdAndSubjectId(userId,subjectId);
        //删除redis里面的数据
        redisTemplate.delete("qwenUserId:"+userId+":subjectId:"+subjectId);

        //删除userMessage
        Subject subject=new Subject();
        subject.setUserId(userId);
        subject.setSubjectId(subjectId);

        //TODO 处理messageMapper
//        userMessageMapper.deleteChatMessageByUserIdAndSubjectId(subject);
        com.xhq.api.dto.Subject subject1=new com.xhq.api.dto.Subject();
        BeanUtils.copyProperties(subject,subject1);
          messageClient.deleteChatMessageByUserIdAndSubjectId(subject1);

        //延迟双删
        redisTemplate.delete("qwenUserId:"+userId+":subjectId:"+subjectId);

    }

}




