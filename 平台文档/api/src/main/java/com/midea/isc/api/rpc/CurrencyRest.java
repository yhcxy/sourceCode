package com.midea.isc.api.rpc;

import com.midea.isc.api.biz.CurrencyBiz;
import com.midea.isc.api.biz.CurrencyFormatBiz;
import com.midea.isc.api.model.CurrencyFormat;
import com.midea.isc.api.model.Message;
import com.midea.isc.api.param.CurrencyFormatParam;
import com.midea.isc.api.param.CurrencyParam;
import com.midea.isc.api.vo.FormatVo;
import com.midea.isc.auth.client.annotation.IgnoreClientToken;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.web.rpc.BasicRest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@Api(value = "币种格式API", tags = {"currency", "format"})
@RestController
@RequestMapping("currency")
public class CurrencyRest extends BasicRest {

    @Autowired
    CurrencyFormatBiz currencyFormatBiz;


    @Autowired
    CurrencyBiz currencyBiz;


    @ApiOperation("根据国家和币种查格式化")
    @RequestMapping(value = "/getFormat", method = RequestMethod.GET)
    public FormatVo getFormat(@ApiParam(value = "国家/地区", required = true) @RequestParam String territory,
                              @ApiParam(value = "币种", required = true) @RequestParam String currency) throws IscException {
        FormatVo format = new FormatVo();
        //定制化查询
        CurrencyFormatParam param = new CurrencyFormatParam();
        param.setTerritoryCode(territory);
        param.setCurrencyCode(currency);
        format = currencyFormatBiz.selectOneWithCache(param);
        //默认查询
        if (StringUtils.isEmpty(format.getCurrencyCode())) {
            CurrencyParam currencyParam = new CurrencyParam();
            currencyParam.setCode(currency);
            format = currencyBiz.selectOneWithCache(currencyParam);
        }
        return format;
    }

}
