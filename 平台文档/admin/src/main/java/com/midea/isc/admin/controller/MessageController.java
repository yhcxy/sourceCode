package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.MessageBiz;
import com.midea.isc.admin.model.Message;
import com.midea.isc.admin.param.MessageParam;
import com.midea.isc.admin.vo.MessageParamVo;
import com.midea.isc.common.web.contoller.BasicController;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/message")
public class MessageController extends BasicController<MessageBiz, Message, MessageParam> {
    @RequestMapping(value = "/getAppMessage", method = RequestMethod.POST)
    public List<Message> getAppMessage(@RequestBody MessageParamVo param) {
        return baseBiz.getAppMessage(param);
    }

    @RequestMapping(value = "/totalAppMessage", method = RequestMethod.POST)
    public int totalAppMessage(@RequestBody MessageParamVo param) {
        return baseBiz.totalAppMessage(param);
    }
}