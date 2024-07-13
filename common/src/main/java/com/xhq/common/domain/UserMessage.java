package com.xhq.common.domain;


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
public class UserMessage implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private Long userId;
    private Long subjectId;
    private String role;
    private String content;
    private Date createTime;
    private Date updateTime;
    // wait running succeeed failed
    private String status;
    private Integer isDelete;
    private String answerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessage that = (UserMessage) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(subjectId, that.subjectId) && Objects.equals(role, that.role) && Objects.equals(content, that.content) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(status, that.status) && Objects.equals(isDelete, that.isDelete) && Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, subjectId, role, content, createTime, updateTime, status, isDelete, answerId);
    }
}

