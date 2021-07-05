package com.lzc.pojo;

import com.google.common.base.Objects;
import lombok.*;

import java.io.Serializable;

/**
 * @author Liang Zhancheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentDO implements Serializable {

    private static final long serialVersionUID = -8869222996582667606L;

    private Integer id;
    private Integer userId;
    private String note;
    private String gmtCreate;
    private String gmtModified;

    public CommentDO(Integer id, Integer userId, String note) {
        this.id = id;
        this.userId = userId;
        this.note = note;
    }

    public CommentDO(Integer userId, String note) {
        this.userId = userId;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentDO comment = (CommentDO) o;
        return Objects.equal(id, comment.id) && Objects.equal(userId, comment.userId) && Objects.equal(note, comment.note) && Objects.equal(gmtCreate, comment.gmtCreate) && Objects.equal(gmtModified, comment.gmtModified);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, note, gmtCreate, gmtModified);
    }
}
