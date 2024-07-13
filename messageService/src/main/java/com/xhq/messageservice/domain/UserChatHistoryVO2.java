package com.xhq.messageservice.domain;

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
public class UserChatHistoryVO2 {
    Long userId;
    Long subjectId;
    List<UserMessage2>  chatMessageList;
}
