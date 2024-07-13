package com.xhq.messageservice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AnswerVO {
    String userId;
    Long SubjectId;
    String answer;
}
