package com.msa4hipgram.domain.post.mapper;

import com.msa4hipgram.domain.post.entities.PostMybatis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<PostMybatis> getPagination(int limit, int offset);
    Long getTotal();
    PostMybatis findByPk(long id);
    long countPostsByUserId(long userId);
}
