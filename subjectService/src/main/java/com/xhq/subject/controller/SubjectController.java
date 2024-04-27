package com.xhq.subject.controller;


import com.xhq.common.domain.Result;
import com.xhq.common.utils.HostHolder;
import com.xhq.subject.domain.Subject;
import com.xhq.subject.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController()
@RequestMapping("/api/subject")
@Api(tags = "subject有关的的controller")
public class SubjectController {


    @Autowired
    private SubjectService subjectService;

    /**
     * 获取当前用户的查询历史主题列表
     * @param
     * @return
     */
    @GetMapping("/getUserSubjectList")
    @ApiOperation("获取当前用户的查询历史主题列表")
    public Result<List<Subject>> getUserSubjectList() {
        List<Subject> subjectList = subjectService.getUserSubjectList();
        return Result.success(subjectList);
    }


    @PostMapping("/newSubject")
    @ApiOperation("新建一个subject")
    public Result<Long> newSubject(@RequestBody Subject subject){
        Long id=subjectService.newSubject(subject);

        return Result.success(id);
    }

    @GetMapping("/deleteChatHistoryBySubjectId")
    public Result deleteChatHistoryBySubjectId(@RequestParam(value = "subjectId") Long subjectId){
        subjectService.deleteChatHistoryBySubjectId(subjectId);
        return Result.success();
    }

    @GetMapping("/hello")
    public String hello(){
        System.out.println("hello:"+HostHolder.getUser());
        System.out.println("subjectHello");
        return "hello";

    }

    @GetMapping("/hello2")
    public String hello2(){
        return "hello2";
    }
}
