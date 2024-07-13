package com.xhq.messageservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhq.messageservice.domain.UserMessage;
import com.xhq.messageservice.domain.UserMessage2;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMessageMapper extends BaseMapper<UserMessage> {

    @Insert("insert into message(user_id,subject_id,role,content) values(#{userId},#{subjectId},#{role},#{content})")
    void insert(Long userId, Long subjectId, String role, String content);

    @Select("select * from message where user_id=#{userId} and subject_id=#{subjectId}")
    List<UserMessage> getUserMessageByUserIdAndSubjectId(Long userId, Long subjectId);

    @Delete("delete from message where user_id=#{userId} and subject_id=#{subjectId}")
    void deleteChatMessageByUserIdAndSubjectId(Long userId, Long subjectId);

    @Select("select * from message where userId=#{userId} and role=`user` and (status=`wait` or status=`running`) order by update_time limit 1")
    UserMessage selectLastQuestion(Long userId);

    @Select("select * from message where id=#{messageId}")
    UserMessage getUserMessageByMessageId(String messageId);
}
