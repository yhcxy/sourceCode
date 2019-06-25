package com.midea.isc.api.rpc;

import com.midea.isc.api.biz.CodeConfigBiz;
import com.midea.isc.api.model.CodeConfigcache;
import com.midea.isc.api.vo.SequenceCodeParamVo;
import com.midea.isc.auth.client.annotation.IgnoreClientToken;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.web.rpc.BasicRest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "业务系统编码", tags = {"codeConfig", "业务系统编码"})
@RestController
@RequestMapping("codeGenerate")
public class CodeGenerateRest extends BasicRest {

    @Autowired
    private CodeConfigBiz codeconfigBiz;

    @ApiOperation("编码生成")
    @IgnoreClientToken
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public List<String> getSequenceCode(@RequestBody SequenceCodeParamVo vo) {
        //检查参数有效性
        checkVo(vo);
        //设置默认查询数量
        if (null == vo.getQuantity() || 1 > vo.getQuantity()) {
            vo.setQuantity(1);
        }
        //生成序列号
        return codeconfigBiz.createSequenceCode(vo);
    }

    @ApiOperation("删除缓存")
    @IgnoreClientToken
    @RequestMapping(value = "/synchronize", method = RequestMethod.POST)
    public boolean synchronize(@ApiParam(value = "应用名称", required = true) String application,
                               @ApiParam(value = "编码", required = true) String code) {
        return codeconfigBiz.synchronize(application, code);
    }

    @ApiOperation("重置序列号")
    @IgnoreClientToken
    @RequestMapping(value = "/resetSeequence", method = RequestMethod.POST)
    public boolean resetSeequence(@ApiParam(value = "应用名称", required = true) @RequestParam("application") String application,
                                  @ApiParam(value = "编码", required = true) @RequestParam("code") String code,
                                  @ApiParam(value = "表达式", required = true) @RequestParam("expression") String expression,
                                  @ApiParam(value = "序列", required = true) @RequestParam("sequence") int sequence) {
        return codeconfigBiz.resetSeequence(application, code, expression, sequence);
    }

    @ApiOperation("获取实例序列")
    @IgnoreClientToken
    @RequestMapping(value = "/getInstanceSequence", method = RequestMethod.POST)
    public List<CodeConfigcache> getInstanceSequence(@ApiParam(value = "应用名称", required = true) @RequestParam("application") String application,
                                                     @ApiParam(value = "编码", required = true) @RequestParam("code") String code) {
        return codeconfigBiz.getInstanceSequence(application, code);
    }

    /**
     * 检查参数有效性
     *
     * @param vo
     */
    private void checkVo(SequenceCodeParamVo vo) {
        if (StringUtils.isBlank(vo.getApplication())) {
            throw new IscException("CG-001");
        }
        if (StringUtils.isBlank(vo.getCode())) {
            throw new IscException("CG-002");
        }
    }

}