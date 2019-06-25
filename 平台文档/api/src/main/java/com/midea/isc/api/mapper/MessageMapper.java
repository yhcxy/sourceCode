package com.midea.isc.api.mapper;

import com.midea.isc.api.model.Message;
import com.midea.isc.api.param.MessageParam;
import com.midea.isc.common.mapper.BaseMapper;

public interface MessageMapper extends BaseMapper<Message, MessageParam> {

    // 多语言操作

    public int insertTL(Message model);

    public int updateTL(Message model);

    public int updateFieldsTL(Message model);

    public int copySourceLang(Message model);

}
