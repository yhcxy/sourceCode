package com.midea.isc.api.vo;

import lombok.Data;

import java.util.HashMap;

/**
 * 编码生成参数
 *
 * @author ex_liws1
 * @since 2019-06-21
 */
@Data
public class SequenceCodeParamVo {

    /**
     * 系统名称
     */
    private String application;
    /**
     * 编码
     */
    private String code;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 参数
     */
    private HashMap<String, String> params;

}
