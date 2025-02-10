package com.harmoni.auth.mapper;

import com.harmoni.auth.model.UserRoleKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);
    List<UserRoleKey> selectRolesByUserId(Integer userId);
    int insert(UserRoleKey row);
}