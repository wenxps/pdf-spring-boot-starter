package com.wenx.pdfspringbootstarter.config;

import com.wenx.pdfspringbootstarter.properties.PdfProperties;
import com.wenx.pdfspringbootstarter.utils.PdfTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 第二步：设置自动装配类
 * 第三步：spring.factories

 * @author 温笙
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PdfProperties.class)
public class PdfTemplateAutoConfig {

	@Configuration
	static class PdfTemplateConfig {
		private final PdfProperties properties;
		PdfTemplateConfig (PdfProperties pdfProperties){
			this.properties = pdfProperties;
		}

		@Bean
		@Primary
		@ConditionalOnMissingBean
		public PdfTemplate pdfTemplate() {
			return new PdfTemplate(this.properties);
		}
	}
}