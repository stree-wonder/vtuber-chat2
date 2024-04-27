package com.xhq.messageservice.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="message")
public class UserMessage implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer subjectId;
    private String role;
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessage message1 = (UserMessage) o;
        return Objects.equals(id, message1.id) && Objects.equals(userId, message1.userId) && Objects.equals(subjectId, message1.subjectId) && Objects.equals(role, message1.role) && Objects.equals(content, message1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, subjectId, role, content);
    }
}

