package com.xhq.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * 
 * @TableName prompt
 */
@TableName(value ="prompt")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prompt implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private String id;
    private String name;
    /**
     * 
     */
    private String promptText;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prompt prompt = (Prompt) o;
        return Objects.equals(id, prompt.id) && Objects.equals(name, prompt.name) && Objects.equals(promptText, prompt.promptText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, promptText);
    }

    @Override
    public String toString() {
        return "Prompt{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", promptText='" + promptText + '\'' +
                '}';
    }
}