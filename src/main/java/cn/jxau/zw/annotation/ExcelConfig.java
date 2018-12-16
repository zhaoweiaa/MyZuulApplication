package cn.jxau.zw.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义excel表格字段配置注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelConfig {

    /**
     * 导出字段名
     * @return
     */
    public String exportName() default "默认标题";

    /**
     * 列宽 （1-255字符）
     * @return
     */
    public int exportFieldWith() default 20;

    /**
     * 是否转换字段
     * 若是sign为true,则需要在pojo中加入一个方法 get字段名Convert()
     * 例如，字段sex ，需要加入 public String getSexConvert() 返回值为string
     * @return
     */
    public boolean exportConvertSign() default false;

    /**
     * 导入数据是否需要转化 及 对已有的excel，是否需要将字段转为对应的数据
     * 若是sign为true,则需要在pojo中加入 void set字段名Convert(String text)
     * @return
     */
    public boolean importConvertSign() default false;

//    /**
//     * 是否求和
//     * @return
//     */
//    public boolean isSum() default false;

//    /**
//     * 保留小数位
//     * @return
//     */
//    public int scale() default 0;

//    /**
//     * 是否合并相同行，在同一列中存在相同的值时是否合并行，默认不合并
//     * @return
//     */
//    public boolean isMerege() default false;

//    /**
//     * 合并判断字段，如果合并相同行，判断值是否相等的字段，默认为当前字段。
//     * 如：每个人的姓名不是唯一的，但身份证是唯一的，当出现姓名相同是，可通过身份证判断是否合并
//     * @return
//     */
//    public String meregeFlag() default "";
}
