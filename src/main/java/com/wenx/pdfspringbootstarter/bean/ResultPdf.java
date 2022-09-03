package com.wenx.pdfspringbootstarter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.InputStream;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ResultPdf {
    public String name;
    public String age;
    public InputStream image;
}