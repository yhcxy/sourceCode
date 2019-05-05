/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.antmeite.api.demo.member.impl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antmeite.api.demo.member.IDemoMemberService;
import com.antmeite.code.base.BaseApiService;
import com.antmeite.code.base.ResponseBase;
import com.antmeite.code.base.ResponseBase;
import com.antmeite.code.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年9月24日 上午8:51:13<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
@RestController
@Api(description = "会员服务接口")
public class DemoMemberServiceImpl extends BaseApiService implements IDemoMemberService {

	@ApiOperation("根据用户userId查询会员信息")
	@ApiResponse(code = 200, message = "查询结果成功")
	@ApiImplicitParam(name = "userId", value = "用户userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "获取数据成功"), @ApiResponse(code = 400, message = "参数错误"), })
	@GetMapping("/demoMember")
	public ResponseBase demoMember(@RequestParam("userId") Long userId) {
		return setResultSuccess("会员服务,微服务电商项目基本环境搭建成功...");
	}

}
