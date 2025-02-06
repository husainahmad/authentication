package com.harmoni.auth.mapper;

import com.harmoni.auth.model.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper {
    int insert(Permission row);
    Permission selectByPrimaryKey(Long id);
    int updateByPrimaryKey(Permission row);
    int deleteByPrimaryKey(Long id);
}