package com.harmoni.auth.mapper;

import com.harmoni.auth.model.RolePermissionKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionMapper {
    int deleteByPrimaryKey(RolePermissionKey key);
    int insert(RolePermissionKey row);
}