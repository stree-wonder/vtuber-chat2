package com.xhq.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AskDto {

    String question;

    Integer subjectId;
}
