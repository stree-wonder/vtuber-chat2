package com.xhq.messageservice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AskDto {

    String question;

    Long subjectId;
}
