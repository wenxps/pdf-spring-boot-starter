package com.wenx.pdfspringbootstarter.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.InputStream;
import java.io.OutputStream;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public String name;
    public ImageBean imageBean;
    public Integer age;
}