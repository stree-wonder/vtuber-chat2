package com.xhq.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhq.userservice.domain.Subject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author mytx
* @description 针对表【subject】的数据库操作Mapper
* @createDate 2024-03-18 21:09:20
* @Entity generator.domain.Subject
*/
@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {

    void newSubject(@Param("subject") Subject subject);

    @Select("select * from subject where user_id=#{userId}")
    List<Subject> getUserSubjectList(Long userId);

    @Delete("delete from subject where user_id=#{userId} and subject_id=#{subjectId}")
    void deleteByUserIdAndSubjectId(Long userId, Long subjectId);
}




