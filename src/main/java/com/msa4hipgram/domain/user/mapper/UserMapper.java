package com.msa4hipgram.domain.user.mapper;

import com.msa4hipgram.domain.user.entities.UserMybatis;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserMybatis findByPk(long id);
    UserMybatis findByEmail(String email);
}
