package com.yehui.mapper;

import com.yehui.entity.TbUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDataSourceMapper {
    public List<TbUser> findUser();
}
