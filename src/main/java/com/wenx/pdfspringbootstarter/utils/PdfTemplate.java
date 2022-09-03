package com.wenx.pdfspringbootstarter.utils;

import cn.hutool.log.Log;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.wenx.pdfspringbootstarter.bean.User;
import com.wenx.pdfspringbootstarter.properties.PdfProperties;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

/**
 * @author 温笙
 */
@Data
@ToString
public class PdfTemplate {

    private final Log log = Log.get(PdfTemplate.class);

    /**
     * 模板文件路径
     */
    private String TEMPLATE_NAME;
    /**
     * 生成的文件路径
     */
    private String OUT_FILE_NAME;

    private String FONT_FILE_NAME;
    public PdfTemplate(){}
    public PdfTemplate(PdfProperties properties){
        this.TEMPLATE_NAME = properties.getTemplateFile();
        this.OUT_FILE_NAME = properties.getOutFolder();
        this.FONT_FILE_NAME = properties.getFontFile();
    }

    private static OutputStream os = null;
    private static PdfStamper ps = null;
    private static PdfReader reader = null;
    private static PdfStamper stamper = null;
    private static Object data;

    /**
     * 文件夹不存在则创建
     * @param folder
     */
    public void noExistCreate(String folder){
        File file = new File(this.OUT_FILE_NAME);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 仅支持文字域填充
     * @param sourceMap 数据 k->v
     * @param createFileName 生成文件名
     */
    public void importTemplateDataSimple(Map<String,String> sourceMap,String createFileName){

        // 生成文件夹不存在则创建
        noExistCreate(OUT_FILE_NAME);

        try {

            // 1 打开模板文件
            os = Files.newOutputStream(new File(this.OUT_FILE_NAME+File.separator+createFileName+".pdf").toPath());
            // 2 读入pdf表单
            reader = new PdfReader(this.TEMPLATE_NAME);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 <b>【字体需要放在resources目录下】</b>
            BaseFont bf = BaseFont.createFont(FONT_FILE_NAME, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(bf);
            // 6查询数据================================================
            // 7遍历data 给pdf表单表格赋值
            for (String key : sourceMap.keySet()) {
                form.setField(key, sourceMap.get(key).toString());
            }
            ps.setFormFlattening(true);
            log.info("===============PDF导出成功=============");
        } catch (Exception e) {
            log.error(e,"===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
                assert ps != null;
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将对象数据填充到模板中 支持文字
     * <p>对象字段类型支持字符串，数字</p>
     * @param data 对象数据
     */
    public void importTemplateDataSimple(Object data,String createFileName) throws InstantiationException, IllegalAccessException {
        noExistCreate(OUT_FILE_NAME);
        try {
            os = Files.newOutputStream(new File(this.OUT_FILE_NAME+File.separator+createFileName+".pdf").toPath());
            // 2 读入pdf表单
            reader = new PdfReader(TEMPLATE_NAME);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 <b>【字体需要放在resources目录下】</b>
            BaseFont bf = BaseFont.createFont(FONT_FILE_NAME, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(bf);
            // 6查询数据================================================
            // 7遍历data 给pdf表单表格赋值
            // todo 获取到类中所有字段进行设置
            // TODO: 2022/9/1  通过反射获取对象的所有字段，是一个数组
            Field[] declaredFields = data.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                //设置是否可以访问，如果不设置将报错
                declaredField.setAccessible(true);
                form.setField(declaredField.getName(), declaredField.get(data).toString());
            }
            ps.setFormFlattening(true);
            System.out.println("===============PDF模板导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
                assert ps != null;
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public <T> void importTemplateDataSimpleWithImg(Object data,String createFileName) throws InstantiationException, IllegalAccessException, IOException, DocumentException {
        noExistCreate(OUT_FILE_NAME);
        PdfReader reader;
        OutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper = null;
        try {
            // 5给表单添加中文字体 <b>【字体需要放在resources目录下】</b>
            BaseFont bf = BaseFont.createFont(this.FONT_FILE_NAME, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // 输出流
            out = Files.newOutputStream(new File(this.OUT_FILE_NAME+File.separator+createFileName+".pdf").toPath());
            // 读取pdf模板
            reader = new PdfReader(TEMPLATE_NAME);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bf);
            // 6查询数据================================================
            // 7遍历data 给pdf表单表格赋值
            // todo 获取到类中所有字段进行设置
            // TODO: 2022/9/1  通过反射获取对象的所有字段，是一个数组
            Field[] declaredFields = data.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                //设置是否可以访问，如果不设置将报错
                declaredField.setAccessible(true);
                System.out.println("字段名称：" + declaredField.getName());
                System.out.println("字段值：" + declaredField.get(data));
                T imageClass = (T) declaredField.get(data);
                if(imageClass instanceof InputStream){
                    byte[] imgBytes = toByteArray((InputStream) imageClass);
                    if(imgBytes != null){
                        int pageNo = form.getFieldPositions(declaredField.getName()).get(0).page;
                        Rectangle signRect = form.getFieldPositions(declaredField.getName()).get(0).position;
                        float x = signRect.getLeft();
                        float y = signRect.getBottom();

                        Image image = Image.getInstance(imgBytes);
                        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                        image.setAbsolutePosition(x, y);
                        //  getOverContent 图片会覆盖在文字上层
                        PdfContentByte underContent = stamper.getOverContent(pageNo);
                        // 指定图片的宽高
                        underContent.addImage(image);
                    }

                    // // TODO: 2022/9/2
                    stamper.setFormFlattening(true);
                    stamper.close();
                    Document doc = new Document();
                    Font font = new Font(bf, 32);
                    PdfCopy copy = new PdfCopy(doc, out);

                    doc.open();
                    PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
                    copy.addPage(importPage);
                    doc.close();


                }else{
                    stamper.setFormFlattening(true);
                    form.setField(declaredField.getName(), declaredField.get(data).toString());
                }
            }

            System.out.println("===============PDF模板导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
                stamper.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * InputStream 转 byte[]
     * @param input
     * @return
     */
    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }


    public void toConvertPDF(User user) {
        noExistCreate(OUT_FILE_NAME);
        try {
            Date date = new Date();
            /* 打开已经定义好字段以后的pdf模板 */
            PdfReader reader = new PdfReader(this.TEMPLATE_NAME);
            /* 将要生成的目标PDF文件名称 */
            PdfStamper stamp = new PdfStamper(reader, Files.newOutputStream(Paths.get(this.OUT_FILE_NAME + File.separator + user.getName() + ".pdf")));
            /* 使用中文字体（华文宋体） */
            BaseFont bf = BaseFont.createFont(this.FONT_FILE_NAME,
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            /* 取出报表模板中的所有字段 */
            AcroFields form = stamp.getAcroFields();
            form.addSubstitutionFont(bf);

            /* 为字段赋值,注意字段名称是区分大小写的 */
            form.setField("name", user.getName());
            form.setField("age", user.getAge().toString());
            addImages(form,stamp,"image","G:\\IDEA\\pdf-convert\\src\\main\\resources\\images\\123.jpg");
            stamp.setFormFlattening(true);
            /* 必须要调用这个，否则文档不会生成的 */
            stamp.close();
            reader.close();
        } catch (DocumentException | IOException ignored) {
        }
    }

    /**
     * 插入图片方法
     *
     * @param form     取出报表模板中的所有字段
     * @param stamp    将要生成的目标PDF文件名称
     * @param name     图片域名
     * @param filename 要插入的图片相对路径+名称
     * @throws DocumentException
     * @throws IOException
     */
    public void addImages(AcroFields form, PdfStamper stamp, String name, String filename) throws DocumentException, IOException {
        /*插入图片*/
        int pageNo = form.getFieldPositions(name).get(0).page;
        Rectangle signRect = form.getFieldPositions(name).get(0).position;
        float x = signRect.getLeft();
        float y = signRect.getBottom();
        Image image = Image.getInstance(filename);
//            /*获取操作的页面*/
        PdfContentByte under = stamp.getOverContent(pageNo);
//            /*根据域的大小缩放图片*/
        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
        /*添加图片*/
        image.setAbsolutePosition(x, y);
        under.addImage(image);
    }



}