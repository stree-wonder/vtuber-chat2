package com.xhq.promptservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhq.promptservice.domain.Prompt;
import org.apache.ibatis.annotations.Select;

/**
* @author mytx
* @description 针对表【prompt】的数据库操作Mapper
* @createDate 2024-03-18 21:09:04
* @Entity generator.domain.Prompt
*/

public interface PromptMapper extends BaseMapper<Prompt> {
    @Select("select prompt_text from prompt where name=#{name}")
    String Prompt_get(String name);

    @Select("select * from prompt where id=#{promptId}")
    Prompt getById(int promptId);

}




