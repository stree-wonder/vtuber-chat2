package com.xhq.api.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPromoptChatDTO {
    private int subjectId;
    private int promptId;
    private String subject;
}
