package com.xhq.messageservice.domain;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPromoptChatDTO {
    private Long subjectId;
    private Long promptId;
    private String subject;
}
