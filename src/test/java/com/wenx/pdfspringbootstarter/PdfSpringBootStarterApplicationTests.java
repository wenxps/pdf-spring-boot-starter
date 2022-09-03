package com.wenx.pdfspringbootstarter;

import com.itextpdf.text.DocumentException;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.wenx.pdfspringbootstarter.bean.ResultPdf;
import com.wenx.pdfspringbootstarter.bean.User;
import com.wenx.pdfspringbootstarter.properties.PdfProperties;
import com.wenx.pdfspringbootstarter.utils.PdfTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class PdfSpringBootStarterApplicationTests {

    @Autowired
    PdfProperties properties;

    @Test
    void contextLoads() throws IOException {

        PdfTemplate pdfTemplate = new PdfTemplate(properties);
        System.out.println(pdfTemplate);
        pdfTemplate.toConvertPDF(new User("zs1",null,20));


    }

    @Test
    public void simpleMap(){
        PdfTemplate pdfTemplate = new PdfTemplate(properties);
        Map<String,String> map = new HashMap<>();
        map.put("name","xiao");
        map.put("age","20");
        pdfTemplate.importTemplateDataSimple(map,"xiaoming");
    }

    @Test
    public void simpleClassImg() throws IOException, InstantiationException, IllegalAccessException, DocumentException {
        PdfTemplate pdfTemplate = new PdfTemplate(properties);
        ResultPdf resultPdf = new ResultPdf();
        resultPdf.setAge("20");
        resultPdf.setName("XI");
        resultPdf.setImage(Files.newInputStream(Paths.get("G:\\IDEA\\pdf-convert\\src\\main\\resources\\images\\123.jpg")));
        pdfTemplate.importTemplateDataSimpleWithImg(resultPdf,"resultPdf");
    }

}