package com.wenx.pdfspringbootstarter.utils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.wenx.pdfspringbootstarter.bean.ImageBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

@Slf4j
public class PDFUtil {
    /**
     * PDF模板填充数据生成PDF文档
     *
     * @param templatePath PDF模板路径
     * @param outputPath   生成文档模板路径
     */
    public static <T> void toConvertPDF(String templatePath, String outputPath, Object data,Class<T> aClass) throws IllegalAccessException {
        System.out.println("outputPath:"+outputPath);
        try {
            /* 打开已经定义好字段以后的pdf模板 */
            PdfReader reader = new PdfReader(templatePath);
            /* 将要生成的目标PDF文件名称 */
            PdfStamper stamp = new PdfStamper(reader, Files.newOutputStream(Paths.get(outputPath)));

            /* 使用中文字体（华文宋体） */
            BaseFont bf = BaseFont.createFont("fonts/youhuazi.ttf",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            /* 取出报表模板中的所有字段 */
            AcroFields form = stamp.getAcroFields();
            form.addSubstitutionFont(bf);

            //取出数据
            Field[] declaredFields = data.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {

                //图片字段 image
                if(declaredField.get(data) instanceof ImageBean){
                    System.out.println("图片字段:"+declaredField.getName());
                    ImageBean imageBean = (ImageBean) declaredField.get(data);
                    String path = imageBean.getPath();
                    String pdfParam = imageBean.getParam();
//                    addImagesStream(form,stamp,pdfParam,path);
                }else{
                    /* 为字段赋值,注意字段名称是区分大小写的 */
                    form.setField(declaredField.getName(),declaredField.get(data).toString());
                }
            }
            stamp.setFormFlattening(true);
            /* 必须要调用这个，否则文档不会生成的 */
            stamp.close();
            reader.close();
        } catch (DocumentException | IOException de) {
            log.info(de.getMessage());
        }
    }

    /**
     * 日期格式化
     *
     * @param date 日期
     * @return 返回字符串格式日期
     */
    public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }


    /**
     * 插入图片方法
     *
     * @param form     取出报表模板中的所有字段
     * @param stamp    将要生成的目标PDF文件名称
     * @param name     图片域名
     * @param filename 要插入的图片相对路径+名称
     */
    public static void addImages(AcroFields form, PdfStamper stamp, String name, String filename) throws DocumentException, IOException {
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

    /**
     * 将输入流转化成byte数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] toArrayByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int temp;
        while ((temp = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, temp);
        }
        return bytes;
    }

    /*--------------------------------以下为生成条形码的代码-----------------------------------------------*/
    public static void generateBarCode128(String msg, boolean hideText, OutputStream ous) {
        generateBarCode(msg, hideText, ous, new Code128Bean());
    }

    /**
     * 已生成code128条形码为例
     *
     * @param msg      要生成的文本
     * @param hideText 隐藏可读文本
     * @param ous
     */
    public static void generateBarCode(String msg, boolean hideText, OutputStream ous, AbstractBarcodeBean bean) {
        try {
            if (StringUtils.isEmpty(msg) || ous == null) {
                return;
            }
            // 如果想要其他类型的条码(CODE 39, EAN-8...)直接获取相关对象Code39Bean...等等
//            AbstractBarcodeBean bean = new Code128Bean();
            // 分辨率：值越大条码越长，分辨率越高。
            int dpi = 150;
            // 设置两侧是否加空白
            bean.doQuietZone(true);
            // 设置条码每一条的宽度
            // UnitConv 是barcode4j 提供的单位转换的实体类，用于毫米mm,像素px,英寸in,点pt之间的转换
            bean.setModuleWidth(UnitConv.in2mm(3.0f / dpi));

            // 设置文本位置（包括是否显示）
            if (hideText) {
                bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
            }
            // 设置图片类型
            String format = "image/png";

            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生产条形码
            bean.generateBarcode(canvas, msg);

            // 结束
            canvas.finish();
            ous.close();
        } catch (IOException ie) {
            ie.getStackTrace();
        }
    }

    /**
     * 生成条码文件
     *
     * @param msg      要生成的字符串
     * @param hideText
     * @param path     生成条形码存放路径+名称
     * @return
     */
    public static File generateFile(String msg, boolean hideText, String path) {
        File file = new File(path);
        try {
            generateBarCode128(msg, hideText, new FileOutputStream(file));
        } catch (FileNotFoundException fe) {
            throw new RuntimeException(fe);
        }
        return file;
    }
}