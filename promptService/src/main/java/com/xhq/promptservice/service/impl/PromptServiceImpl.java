package com.xhq.promptservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhq.common.utils.HostHolder;
import com.xhq.promptservice.domain.Prompt;
import com.xhq.promptservice.mapper.PromptMapper;
import com.xhq.promptservice.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
* @author mytx
* @description 针对表【prompt】的数据库操作Service实现
* @createDate 2024-03-18 21:09:04
*/
@Service
public class PromptServiceImpl extends ServiceImpl<PromptMapper, Prompt>
    implements PromptService {


}




