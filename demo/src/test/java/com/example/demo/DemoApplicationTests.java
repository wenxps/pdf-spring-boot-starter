package com.example.demo;

import com.itextpdf.text.DocumentException;
import com.wenx.pdfspringbootstarter.bean.ResultPdf;
import com.wenx.pdfspringbootstarter.utils.PdfTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NamedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    @Qualifier(value = "pdfTemplate1")
    PdfTemplate pdfTemplate;

    @Test
    void contextLoads() throws IOException, DocumentException, InstantiationException, IllegalAccessException {
        ResultPdf resultPdf = new ResultPdf();
        resultPdf.setAge("20");
        resultPdf.setName("XI");
        resultPdf.setImage(Files.newInputStream(Paths.get("G:\\IDEA\\pdf-convert\\src\\main\\resources\\images\\123.jpg")));
        pdfTemplate.importTemplateDataSimpleWithImg(resultPdf,"resultPdf-image");
    }

    @Autowired
    @Qualifier(value = "pdfTemplate1")
    PdfTemplate pdfTemplate1;

    @Test
    void contextLoads1() throws IOException, DocumentException, InstantiationException, IllegalAccessException {
        ResultPdf resultPdf = new ResultPdf();
        resultPdf.setAge("20");
        resultPdf.setName("XI");
        resultPdf.setImage(Files.newInputStream(Paths.get("G:\\IDEA\\pdf-convert\\src\\main\\resources\\images\\123.jpg")));
        pdfTemplate1.importTemplateDataSimpleWithImg(resultPdf,"resultPdf-image");
    }
    @Autowired
    @Qualifier(value = "pdfTemplate2")
    PdfTemplate pdfTemplate2;
    @Test
    void contextLoads2() throws IOException, DocumentException, InstantiationException, IllegalAccessException {
        ResultPdf resultPdf = new ResultPdf();
        resultPdf.setAge("20");
        resultPdf.setName("XII");
        resultPdf.setImage(Files.newInputStream(Paths.get("G:\\IDEA\\pdf-convert\\src\\main\\resources\\images\\123.jpg")));
        pdfTemplate2.importTemplateDataSimpleWithImg(resultPdf,"resultPdf-image");
    }

}