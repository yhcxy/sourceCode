package com.yehui.mapper;

import com.yehui.annotation.Select;
import com.yehui.model.UserEntity;

public interface UserMapper {

    @Select("SELECT * FROM USER WHERE id = ?")
    public UserEntity getUser(int id);
}
