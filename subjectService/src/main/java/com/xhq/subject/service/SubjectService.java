package com.xhq.subject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhq.subject.domain.Subject;

import java.util.List;

/**
* @author mytx
* @description 针对表【subject】的数据库操作Service
* @createDate 2024-03-18 21:09:20
*/
public interface SubjectService extends IService<Subject> {

    List getUserSubjectList();

    Long newSubject(Subject subject);

    void deleteChatHistoryBySubjectId(Long subjectId);
}
