package com.example.demo.config;

import com.wenx.pdfspringbootstarter.properties.PdfProperties;
import com.wenx.pdfspringbootstarter.utils.PdfTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfig {

    /**
     * com.wenx.pdf.template-file=D:/pdf/source/t1.pdf
     * com.wenx.pdf.out-folder=D:/pdf/target
     * com.wenx.pdf.font-file=D:/pdf/source/fonts/youhuazi.ttf
     * @return
     */
    @Bean("pdfTemplate1")
    PdfTemplate pdfTemplate1(){
        PdfProperties pdfProperties = new PdfProperties("D:/pdf/source/t1.pdf","D:/pdf/target","D:/pdf/source/fonts/youhuazi.ttf");
        return new PdfTemplate(pdfProperties);
    }

    @Bean("pdfTemplate2")
    PdfTemplate pdfTemplate2(){
        PdfProperties pdfProperties = new PdfProperties("D:/pdf/source/t1.pdf","D:/pdf/target","D:/pdf/source/fonts/youhuazi.ttf");
        return new PdfTemplate(pdfProperties);
    }
}