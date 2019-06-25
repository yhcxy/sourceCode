package com.midea.isc.api.vo;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class FormatVo  {
        private Integer formatId;
        private String territoryCode;
        private String currencyCode;
        private String currencySymbol;
        private String description;
        private Integer currencyPrecision;
        private String thousandsSeparator;
        private String decimalSeparator;
        private Integer calculatedPrecision;
        private String enabled;
}
