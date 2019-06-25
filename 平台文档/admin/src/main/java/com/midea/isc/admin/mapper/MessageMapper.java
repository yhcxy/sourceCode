package com.midea.isc.admin.mapper;

import java.util.List;

import com.midea.isc.admin.model.Message;
import com.midea.isc.admin.param.MessageParam;
import com.midea.isc.admin.vo.MessageParamVo;
import com.midea.isc.common.mapper.BaseMapper;

public interface MessageMapper extends BaseMapper<Message, MessageParam> {

    // 多语言操作

    public int insertTL(Message model);

    public int updateTL(Message model);

    public int updateFieldsTL(Message model);

    public int copySourceLang(Message model);

    public List<Message> getAppMessage(MessageParamVo param);

    public int totalAppMessage(MessageParamVo param);

}
