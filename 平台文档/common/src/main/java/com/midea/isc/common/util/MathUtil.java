package com.midea.isc.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    /**
     * 小数加法
     * @author Garmin
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2, RoundingMode mode, int scale){
        return MathUtil.setScale(num1.add(num2), mode, scale);
    }

    /**
     * 小数加法
     * @author Garmin
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal add(double num1, double num2, RoundingMode mode, int scale){
        return MathUtil.setScale(new BigDecimal(num1).add(new BigDecimal(num2)), mode, scale);
    }

    /**
     * 小数减法
     * @author Garmin
     * @param num1 被减数
     * @param num2 减数
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2, RoundingMode mode, int scale){
        return MathUtil.setScale(num1.subtract(num2), mode, scale);
    }

    /**
     * 小数减法
     * @author Garmin
     * @param num1 被减数
     * @param num2 减数
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal subtract(double num1, double num2, RoundingMode mode, int scale){
        return MathUtil.setScale(new BigDecimal(num1).subtract(new BigDecimal(num2)), mode, scale);
    }

    /**
     * 小数乘法
     * @author Garmin
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, RoundingMode mode, int scale){
        return MathUtil.setScale(num1.multiply(num2), mode, scale);
    }

    /**
     * 小数乘法
     * @author Garmin
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal multiply(double num1, double num2, RoundingMode mode, int scale){
        return MathUtil.setScale(new BigDecimal(num1).multiply(new BigDecimal(num2)), mode, scale);
    }

    /**
     * 小数除法
     * @author Garmin
     * @param num1 被除数
     * @param num2 除数
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2, RoundingMode mode, int scale){
        return num1.divide(num2,scale + 8, BigDecimal.ROUND_HALF_UP).setScale(scale, mode);
    }

    /**
     * 小数除法
     * @author Garmin
     * @param num1 被除数
     * @param num2 除数
     * @param mode 取舍方式
     * @param scale 精度
     * @return
     */
    public static BigDecimal divide(double num1, double num2, RoundingMode mode, int scale){
        return new BigDecimal(num1).divide(new BigDecimal(num2),scale + 8, BigDecimal.ROUND_HALF_UP).setScale(scale, mode);
    }

    /**
     * 取精度
     * @author Garmin
     * @param num
     * @param mode
     * @param scale
     * @return
     */
    public static BigDecimal setScale(BigDecimal num, RoundingMode mode, int scale){
        return num.setScale(scale + 8 , BigDecimal.ROUND_HALF_UP).setScale(scale, mode);
    }
}
