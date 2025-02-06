package com.harmoni.auth.mapper;

import com.harmoni.auth.model.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
    int deleteByPrimaryKey(Long id);
    int insert(RefreshToken row);
    RefreshToken selectByPrimaryKey(Long id);
    int updateByPrimaryKey(RefreshToken row);
}