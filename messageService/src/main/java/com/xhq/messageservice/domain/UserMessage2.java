package com.xhq.messageservice.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="message")
public class UserMessage2 implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer subjectId;
    private String role;
    private String answer;
    private String question;
    private Date createTime;
    private Date updateTime;
    private String status;
    private Integer isDelete;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessage2 that = (UserMessage2) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(subjectId, that.subjectId) && Objects.equals(role, that.role) && Objects.equals(answer, that.answer) && Objects.equals(question, that.question) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(status, that.status) && Objects.equals(isDelete, that.isDelete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, subjectId, role, answer, question, createTime, updateTime, status, isDelete);
    }
}

