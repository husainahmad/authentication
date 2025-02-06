package com.harmoni.auth.mapper;

import com.harmoni.auth.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Role row);
    Role selectByPrimaryKey(Long id);
    int updateByPrimaryKey(Role row);
}