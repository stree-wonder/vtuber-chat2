package com.xhq.subject.controller;


import com.xhq.common.domain.Result;
import com.xhq.subject.domain.Prompt;
import com.xhq.subject.service.PromptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/prompt")
@Api(tags = "prompt有关的的controller")
public class PromptController {

    @Autowired
    private PromptService promptService;

    /**
     * 获取当前用户的prompt列表
     * @param
     * @return
     */
    @GetMapping("/getUserPromptList")
    @ApiOperation("获取当前用户的查询历史主题列表")
    public Result<List<Prompt>> getUserPromptList() {
        List<Prompt> PromptList = promptService.list();
        return Result.success(PromptList);
    }

    @ApiOperation("根据id查询prompt信息")
    @GetMapping("/getById")
    public Prompt getById(@RequestParam Long id){
        return promptService.getById(id);
    }



}
