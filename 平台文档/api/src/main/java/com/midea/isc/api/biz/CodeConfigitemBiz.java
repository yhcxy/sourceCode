package com.midea.isc.api.biz;

import com.midea.isc.api.mapper.CodeConfigitemMapper;
import com.midea.isc.api.model.CodeConfigitem;
import com.midea.isc.api.param.CodeConfigitemParam;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CodeConfigitemBiz extends BaseBiz<CodeConfigitemMapper, CodeConfigitem, CodeConfigitemParam> {

    //编码规则类型-入参
    private static final String CG_CONF_ITEM_TYPE_PARAM = "PARAM";
    //编码规则类型-常量
    private static final String CG_CONF_ITEM_TYPE_CONSTANT = "CONSTANT";
    //编码规则类型-日期
    private static final String CG_CONF_ITEM_TYPE_DATE = "DATE";
    //编码规则类型-序列
    public static final String CG_CONF_ITEM_TYPE_SERIAL = "SERIAL";

    public List<CodeConfigitem> getItemsByConfigId(Integer configId) {
        CodeConfigitemParam param = new CodeConfigitemParam();
        param.setConfigId(configId);
        HashMap<String, String> orderBy = new HashMap<>();
        orderBy.put("seq", "asc");
        param.setOrderFields(orderBy);
        return find(param);
    }

    /**
     * 检查items规则是否正确
     *
     * @param items
     * @param code
     * @param params
     */
    public void checkItems(List<CodeConfigitem> items, String code, HashMap params) {
        if (CollectionUtils.isEmpty(items)) {
            //code编码规则不存在
            throw new IscException("CG-004", code);
        }
        for (CodeConfigitem item : items) {
            if (item.getType().equals(CG_CONF_ITEM_TYPE_PARAM)) {
                if (null == params.get(item.getValue())) {
                    //code编码缺少入参条件
                    throw new IscException("CG-005", code);
                }
            }
            if (item.getType().equals(CG_CONF_ITEM_TYPE_DATE)) {
                try {
                    new SimpleDateFormat(item.getValue()).format(Calendar.getInstance().getTime());
                } catch (Exception e) {
                    //code编码日期参数转换失败
                    throw new IscException("CG-006", code);
                }
            }
            if (item.getType().equals(CG_CONF_ITEM_TYPE_SERIAL)) {
                try {
                    Integer.parseInt(item.getValue());
                } catch (NumberFormatException e) {
                    throw new IscException("GC-009", code);
                }
            }
        }
    }


    /**
     * 生成表达式
     *
     * @param items
     * @param params
     * @return
     */
    public String createExpression(List<CodeConfigitem> items, HashMap params) {
        return createSequenceCode(items, params, 0, 1).get(0);
    }

    /**
     * 生成业务编码
     *
     * @param items
     * @param params    参数
     * @param serial    序列号
     * @param qsuantity 数量
     * @return
     */
    public List<String> createSequenceCode(List<CodeConfigitem> items, HashMap params, Integer serial, Integer qsuantity) {
        StringBuilder expression;
        List<String> sequences = new ArrayList<>();

        for (int i = 0; i < qsuantity; i++) {
            expression = new StringBuilder();
            for (CodeConfigitem item : items) {
                if (item.getType().equals(CG_CONF_ITEM_TYPE_PARAM))
                    expression.append(params.get(item.getValue()).toString());
                else if (item.getType().equals(CG_CONF_ITEM_TYPE_DATE))
                    expression.append(new SimpleDateFormat(item.getValue()).format(Calendar.getInstance().getTime()));
                else if (item.getType().equals(CG_CONF_ITEM_TYPE_CONSTANT))
                    expression.append(item.getValue());
                if (item.getType().equals(CG_CONF_ITEM_TYPE_SERIAL) && null != serial && serial > 0) {
                    int size = Integer.parseInt(item.getValue());
                    //0代表前面要补的字符 size代表字符串长度,d表示参数为整数类型
                    expression.append(String.format("%0" + size + "d", serial - i));
                }
            }
            sequences.add(expression.toString());
        }
        //排序翻转
        Collections.reverse(sequences);
        return sequences;
    }


}

