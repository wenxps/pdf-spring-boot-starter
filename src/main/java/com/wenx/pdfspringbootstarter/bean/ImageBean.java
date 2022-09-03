package com.wenx.pdfspringbootstarter.bean;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author 温笙
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageBean {
    public String path;
    public String param;
}