package com.midea.isc.api.rpc;

import com.midea.isc.api.biz.MessageBiz;
import com.midea.isc.api.model.Message;
import com.midea.isc.api.param.MessageParam;
import com.midea.isc.auth.client.annotation.IgnoreClientToken;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.web.rpc.BasicRest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(value = "消息码API", tags = { "Message", "消息码" })
@RestController
@RequestMapping("message")
public class MessageRest extends BasicRest {
	@Autowired
	private MessageBiz messageBiz;

	@ApiOperation("通过模块获取Message数据")
	@IgnoreClientToken
	@RequestMapping(value = "/getByModule", method = RequestMethod.POST)
	public Map<String, List<Message>> getByModule(@ApiParam(value = "应用名称", required = true) String application,
												  @ApiParam(value = "获取语言", required = true) String language,
												  @ApiParam(value = "模块列表,逗号分隔", required = true) String modules) throws IscException {
		Map<String, List<Message>> result = new HashMap<>();
		List<String> moduleList = Arrays.asList(modules.split(","));
		if (!moduleList.isEmpty()) {
			MessageParam param = new MessageParam();
			Profile profile = new Profile();
			profile.set__language(language);
			param.setProfile(profile);
			param.setModule(modules);
			param.setModuleCond("me");
			param.setApplication(application);
			List<Message> queryResult = messageBiz.find(param);

			for (String module : moduleList) {
				List<Message> messages = new ArrayList<>();
				for (Message item : queryResult) {
					if (item.getModule().equals(module)) {
						messages.add(item);
					}
				}
				result.put(module, messages);
			}
		}

		return result;
	}
}
