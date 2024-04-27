package com.xhq.messageservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhq.messageservice.domain.UserMessage;
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


}
