package com.xhq.api.client;

import com.xhq.api.config.DefaultFeignConfig;
import com.xhq.api.dto.Prompt;
import com.xhq.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(value="promptService",configuration = DefaultFeignConfig.class)
public interface promptClient {
//    /**
//     * 获取当前用户的prompt列表
//     * @param
//     * @return
//     */
//    @GetMapping("/api/prompt/getUserPromptList")
//    public Result<List<Prompt>> getUserPromptList();
//
//    @GetMapping("/api/prompt/getById")
//    public Prompt getById(@RequestParam(value = "id") Long id);

}
