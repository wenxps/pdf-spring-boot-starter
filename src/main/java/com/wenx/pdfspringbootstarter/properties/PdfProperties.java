package com.wenx.pdfspringbootstarter.properties;

/**
 * @author 温笙
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 第一步：配置 properties 类，可以设置默认值
 */
@ConfigurationProperties(prefix = "com.wenx.pdf")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PdfProperties {
    public static final String DEFAULT_TEMPLATE_PATH = "G:/pdf/source/template.pdf";
    public static final String DEFAULT_OUT_PATH = "G:/pdf/target";
    public static final String DEFAULT_FONT_PATH = "G:/pdf/source/fonts/youhuazi.ttf";

    private String templateFile = DEFAULT_TEMPLATE_PATH;
    private String outFolder = DEFAULT_OUT_PATH;
    private String fontFile = DEFAULT_FONT_PATH;
}