package com.harmoni.auth.mapper;

import com.harmoni.auth.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(User row);
    int updateByPrimaryKey(User row);
    User selectByPrimaryKey(Integer id);
    User selectByUsername(String username);
    int deleteByPrimaryKey(Integer id);
}