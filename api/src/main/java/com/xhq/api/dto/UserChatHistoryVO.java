package com.xhq.api.dto;

import com.alibaba.dashscope.common.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChatHistoryVO {
    Integer userId;
    Integer subjectId;
    List<Message>  chatMessageList;
}
