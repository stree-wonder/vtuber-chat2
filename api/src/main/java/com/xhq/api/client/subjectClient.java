package com.xhq.api.client;

import com.xhq.api.config.DefaultFeignConfig;
import com.xhq.api.dto.Prompt;
import com.xhq.api.dto.Subject;
import com.xhq.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value="subjectService",configuration = DefaultFeignConfig.class)
public interface subjectClient {

    /**
     * 获取当前用户的查询历史主题列表
     * @param
     * @return
     */
    @GetMapping("/api/subject/getUserSubjectList")
    public Result<List<Subject>> getUserSubjectList();


    @PostMapping("/api/subject/newSubject")
    public Result<Integer> newSubject(@RequestBody Subject subject);

    @GetMapping("/api/subject/deleteChatHistoryBySubjectId")
    public Result deleteChatHistoryBySubjectId(int subjectId);

    @GetMapping("/api/subject/hello")
    public String hello();

    /**
     * 获取当前用户的prompt列表
     * @param
     * @return
     */
    @GetMapping("/api/prompt/getUserPromptList")
    public Result<List<Prompt>> getUserPromptList();

    @GetMapping("/api/prompt/getById")
    public Prompt getById(@RequestParam(value = "id") Long id);

}
