package com.harmoni.auth.mapper;

import com.harmoni.auth.model.UserRoleKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);
    int insert(UserRoleKey row);
}