package com.wenx.pdfspringbootstarter.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTransition;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

/**
 * itext PDF操作
 */
public class PdfSimpleUtils {
    // 文件夹路径
    public static final String FILE_DIR = "G:\\itext\\folder\\";

    // 图片1路径
    public static final String const_JPG_JAVA = FILE_DIR + "1.jpg";

    // 图片2路径
    public static final String const_JPG_NGINX = FILE_DIR + "1.jpg";

    // 变量1
    private static final String const_NEZHA = "哪吒编程";
    private static final String const_NEZHA_PROGRAM = "获取Java学习资料请关注公众号：哪吒编程";
    private static final String const_BIBIDONG = "比比东";
    private static final String const_YUNYUN = "云韵";
    private static final String const_BAIDU = "百度一下 你就知道";
    private static final String const_BAIDU_URL = "https://www.baidu.com";

    private static final String const_PAGE_FIRST = "第一页";
    private static final String const_PAGE_SECOND = "第二页";
    private static final String const_PAGE_THIRD = "第三页";
    private static final String const_PAGE_FOUR = "第四页";
    private static final String const_PAGE_FIVE = "第五页";

    private static final String const_TITLE_FIRST = "一级标题";
    private static final String const_TITLE_SECOND = "二级标题";
    private static final String const_CONTENT = "内容";

    // 定义普通中文字体
    public static Font static_FONT_CHINESE = null;
    // 定义初始化超链字体
    public static Font static_FONT_LINK = null;

    /**
     * 初始化字体
     * @throws IOException
     * @throws DocumentException
     */
    private static void pdfFontInit() throws IOException, DocumentException {
        // 微软雅黑
        BaseFont chinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        // 初始化普通中文字体 Font.BOLD为加粗
        static_FONT_CHINESE = new Font(chinese, 12, Font.NORMAL);
        // 初始化超链字体
        static_FONT_LINK = new Font(chinese, 12, Font.NORMAL, BaseColor.BLUE);
    }

    public static Document document;

    public static void main(String[] args) throws Exception {
        // 初始化字体
        pdfFontInit();
        // 生成一个 PDF 文件
//        createPDF("中文",FILE_DIR + "createPDF.pdf");
//        createPDFWithColor();// 设置PDF的页面大小和背景颜色
        //createPDFWithPassWord();// 创建带密码的PDF
        //createPDFWithNewPages();// 为PDF添加页
        //createPDFWithWaterMark();// 为PDF文件添加水印，背景图
        //createPDFWithContent();//插入块Chunk, 内容Phrase, 段落Paragraph, List
        createPDFWithExtraContent();//插入Anchor, Image, Chapter, Section
        //draw();//画图
        //createPDFWithAlignment();//设置段落
        //createPDFToDeletePage();//删除 page
        //insertPage();// 插入 page
        //splitPDF();//分割 page
        //mergePDF();// 合并 PDF 文件
        //sortpage();// 排序page
        //setHeaderFooter();// 页眉，页脚
        //addColumnText();// 左右文字
        //setView();// 文档视图
        //pdfToZip();// 压缩PDF到Zip
        addAnnotation();// 注释
    }

    /**
     * 创建一个 PDF 文件，并添加文本
     */
    public static void createPDF(String content,String filePath) throws IOException, DocumentException {
        // 实例化 document
        document = new Document();
        // 生成文件
//        String path = FILE_DIR + "createPDF.pdf";
        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }
        PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filePath)));
        // 打开 document
        document.open();
        // 添加文本 此处无法写入中文 TODO
        document.add(new Paragraph(content, static_FONT_CHINESE));
        // 关闭 document
        document.close();
    }

    /**
     * 创建PDF文件，修改文件的属性
     */
    public static void createPDFWithColor() throws FileNotFoundException, DocumentException {
        // 页面大小
        Rectangle rect = new Rectangle(PageSize.A5.rotate());
        // 页面背景色
        rect.setBackgroundColor(BaseColor.YELLOW);
        document = new Document(rect);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "createPDFWithColor.pdf"));
        // PDF版本(默认1.4)
        writer.setPdfVersion(PdfWriter.VERSION_1_6);

        // 文档属性
        document.addAuthor(const_NEZHA);
        document.addTitle("我的第一个pdf");

        // 页边空白
        document.setMargins(10, 10, 10, 10);
        // 打开
        document.open();
        document.add(new Paragraph(const_NEZHA, static_FONT_CHINESE));
        // 关闭
        document.close();
    }

    /**
     * 创建带密码的PDF
     */
    public static void createPDFWithPassWord() throws FileNotFoundException, DocumentException {
        // 页面大小
        Rectangle rect = new Rectangle(PageSize.A5.rotate());
        // 页面背景色
        rect.setBackgroundColor(BaseColor.YELLOW);
        document = new Document(rect);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "createPDFWithPassWord.pdf"));

        // userPassword打开密码："123"
        // ownerPassword编辑密码: "123456"
        writer.setEncryption("123".getBytes(), "123456".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
        document.open();
        document.add(new Paragraph(const_NEZHA, static_FONT_CHINESE));
        document.close();
    }

    /**
     * 为PDF添加页
     */
    public static void createPDFWithNewPages() throws FileNotFoundException, DocumentException {
        document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "createPDFAddNewPages.pdf"));

        document.open();

        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));
        writer.setPageEmpty(true);

        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));

        document.close();
    }

    /**
     * 为PDF文件添加水印，背景图
     */
    public static void createPDFWithWaterMark() throws IOException, DocumentException {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "createPDFWithWaterMark.pdf");

        document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));

        document.close();

        // 图片水印
        PdfReader reader = new PdfReader(FILE_DIR + "createPDFWithWaterMark.pdf");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR + "createPDFWithWaterMark2.pdf"));

        Image img = Image.getInstance(const_JPG_JAVA);
        img.setAbsolutePosition(200, 200);
        PdfContentByte under = stamp.getUnderContent(1);
        under.addImage(img);

        // 文字水印
        PdfContentByte over = stamp.getOverContent(2);

        // 加载字库来完成对字体的创建
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

        over.beginText();

        // 设置颜色 默认为蓝色
        over.setColorFill(BaseColor.RED);
        // 设置字体字号
        over.setFontAndSize(bf, 50);
        // 设置起始位置
        over.setTextMatrix(30, 30);
        // 开始写入水印 左-下-倾斜度
        over.showTextAligned(Element.ALIGN_LEFT, "nezha", 245, 400, 30);
        over.endText();

        // 背景图
        Image img2 = Image.getInstance(const_JPG_NGINX);
        img2.setAbsolutePosition(0, 0);
        PdfContentByte under2 = stamp.getUnderContent(3);
        under2.addImage(img2);

        stamp.close();
        reader.close();

    }

    /**
     * 插入Chunk, Phrase, Paragraph, List
     * Chunk : 块，PDF文档中描述的最小原子元素
     * Phrase : 短语，Chunk的集合
     * Paragraph : 段落，一个有序的Phrase集合
     */
    public static void createPDFWithContent() throws DocumentException, FileNotFoundException {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "createPDFWithContent.pdf");
        document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();

        // 添加块
        document.add(new Chunk(const_NEZHA, static_FONT_CHINESE));
        Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.WHITE);
        Chunk id = new Chunk("springboot", font);
        id.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        id.setTextRise(7);
        document.add(id);
        Font font2 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
        Chunk id2 = new Chunk("springcloud", font2);
        id2.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        id2.setTextRise(3);

        id2.setUnderline(0.2f, -2f);
        document.add(id2);
        document.add(Chunk.NEWLINE);

        // 添加一页，添加短语
        document.newPage();
        document.add(new Phrase("Phrase page"));

        // 添加短语
        Phrase director = new Phrase();
        Chunk name = new Chunk(const_BIBIDONG, static_FONT_CHINESE);
        // 添加下划线（thickness:下划线的粗细，yPosition:下划线离字的距离）
        name.setUnderline(0.5f, -1f);
        director.add(name);
        director.add(new Chunk(","));
        director.add(new Chunk(" "));
        director.add(new Chunk(const_YUNYUN, static_FONT_CHINESE));
        director.setLeading(24);
        document.add(director);

        // 添加一页
        document.newPage();
        Phrase director2 = new Phrase();
        Chunk name2 = new Chunk(const_BIBIDONG, static_FONT_CHINESE);
        name2.setUnderline(0.2f, -2f);
        director2.add(name2);
        director2.add(new Chunk(","));
        director2.add(new Chunk(" "));
        director2.add(new Chunk(const_YUNYUN, static_FONT_CHINESE));
        director2.setLeading(24);
        document.add(director2);

        // 添加段落
        document.newPage();
        document.add(new Paragraph("Paragraph page"));

        Paragraph info = new Paragraph();
        info.add(new Chunk(const_NEZHA));
        info.add(new Chunk(const_BIBIDONG));
        info.add(Chunk.NEWLINE);
        info.add(new Phrase(const_NEZHA));
        info.add(new Phrase(const_NEZHA));
        document.add(info);

        // 通过循环添加段落信息
        document.newPage();
        List list = new List(List.ORDERED);
        for (int i = 0; i < 5; i++) {
            ListItem item = new ListItem(String.format("%s: %d "+const_NEZHA, const_YUNYUN + (i + 1), (i + 1) * 100), new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE));
            List girllist = new List(List.ORDERED, List.ALPHABETICAL);
            girllist.setLowercase(List.LOWERCASE);
            for (int j = 0; j < 3; j++) {
                ListItem girlitem = new ListItem("girls" + (j + 1));
                List rolelist = new List(List.UNORDERED);
                for (int k = 0; k < 2; k++) {
                    rolelist.add(String.format("%s, %s", const_NEZHA + (k + 1), const_BIBIDONG + (k + 1)));
                }
                girlitem.add(rolelist);
                girllist.add(girlitem);
            }
            item.add(girllist);
            list.add(item);
        }
        document.add(list);
        document.close();
    }

    /**
     * 插入锚Anchor, Image, 章节Chapter, 子列表Section
     */
    public static void createPDFWithExtraContent() throws DocumentException, MalformedURLException, IOException {
        //输出一个文件流->文件路径可以不存在
        FileOutputStream out = new FileOutputStream(FILE_DIR + "createPDFWithExtraContent.pdf");
        //创建一个文档
        document = new Document();
        //将文档写入输出流
        PdfWriter.getInstance(document, out);
        //打开文档
        document.open();
        //定义内容
        String content = "you can get anything from : ";
        //定义段落
        Paragraph paragraph = new Paragraph(content);

        // 创建一个链接到外部网站的新锚点
        // 并将此锚点添加到段落中。
        Anchor anchor = new Anchor(const_BAIDU, static_FONT_LINK);
        anchor.setReference(const_BAIDU_URL);
        paragraph.add(anchor);

        //将段落添加到文档
        document.add(paragraph);


        // 开启新的一页
        document.newPage();
        // 创建一个Image对象
        Image img = Image.getInstance(const_JPG_NGINX);
        img.setAlignment(Image.LEFT | Image.TEXTWRAP);
        img.setBorder(Image.BOX);
        img.setBorderWidth(10);
        img.setBorderColor(BaseColor.WHITE);
        // 大小
        img.scaleToFit(800, 50);
        // 旋转
        img.setRotationDegrees(-50);
        document.add(img);

        // 章节Chapter -- 目录
        document.newPage();
        Paragraph title = new Paragraph(const_TITLE_FIRST,static_FONT_CHINESE);
        // 标题和序号
        Chapter chapter = new Chapter(title, 1);

        // 子列表Section
        title = new Paragraph(const_TITLE_SECOND,static_FONT_CHINESE);
        Section section = chapter.addSection(title);
        section.setBookmarkTitle(const_NEZHA);
        section.setIndentation(10);
        section.setBookmarkOpen(false);
        section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

        Section subsection1 = section.addSection(new Paragraph(const_CONTENT,static_FONT_CHINESE));
        subsection1.setIndentationLeft(10);
        subsection1.setNumberDepth(1);
        document.add(chapter);
        document.close();
    }

    /**
     * 画图
     */
    public static void draw() throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "draw.pdf");

        document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();

        // 左右箭头
        document.add(new VerticalPositionMark() {

            public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
            canvas.beginText();
            BaseFont bf = null;
            try {
                bf = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvas.setFontAndSize(bf, 12);

            // LEFT
            canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), llx - 10, y, 0);
            // RIGHT
            canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), urx + 10, y + 8, 180);

            canvas.endText();
            }
        });

        // 直线
        Paragraph p1 = new Paragraph(const_NEZHA ,static_FONT_CHINESE);
        p1.add(new Chunk(new LineSeparator()));
        p1.add(const_BIBIDONG);
        document.add(p1);
        // 点线
        Paragraph p2 = new Paragraph(const_NEZHA ,static_FONT_CHINESE);
        p2.add(new Chunk(new DottedLineSeparator()));
        p2.add(const_BIBIDONG);
        document.add(p2);
        // 下滑线
        LineSeparator UNDERLINE = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
        Paragraph p3 = new Paragraph(const_NEZHA_PROGRAM ,static_FONT_CHINESE);
        p3.add(UNDERLINE);
        document.add(p3);

        document.close();
    }

    /**
     * 设置段落
     */
    public static void createPDFWithAlignment() throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "createPDFWithAlignment.pdf");

        document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();

        Paragraph p = new Paragraph("Never frown, even when you are sad, because you never know who is falling in love with your smile;" +
                "You will see exactly what life is worth, when all the rest has gone;" +
                "It is very simple to be happy, but it is very difficult to be simple.");

        // 默认
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(p);

        // 慢慢的向右移动
        document.newPage();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setIndentationLeft(1 * 15f);
        p.setIndentationRight((5 - 1) * 15f);
        document.add(p);

        // 居右
        document.newPage();
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setSpacingAfter(15f);
        document.add(p);

        // 居左
        document.newPage();
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingBefore(15f);
        document.add(p);

        // 居中
        document.newPage();
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(15f);
        p.setSpacingBefore(15f);
        document.add(p);

        document.close();
    }

    /**
     * 删除页
     */
    public static void createPDFToDeletePage() throws Exception {

        FileOutputStream out = new FileOutputStream(FILE_DIR + "createPDFToDeletePage.pdf");

        document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));

        document.close();

        PdfReader reader = new PdfReader(FILE_DIR + "createPDFToDeletePage.pdf");
        reader.selectPages("1,3");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR + "createPDFToDeletePage2.pdf"));
        stamp.close();
        reader.close();
    }

    /**
     * 插入 page
     */
    public static void insertPage() throws Exception {

        FileOutputStream out = new FileOutputStream(FILE_DIR + "insertPage.pdf");

        document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));

        document.close();

        PdfReader reader = new PdfReader(FILE_DIR + "insertPage.pdf");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR + "insertPage2.pdf"));

        stamp.insertPage(2, reader.getPageSize(1));

        ColumnText ct = new ColumnText(null);
        ct.addElement(new Paragraph(24, new Chunk("INSERT PAGE")));
        ct.setCanvas(stamp.getOverContent(2));
        ct.setSimpleColumn(36, 36, 559, 770);

        stamp.close();
        reader.close();
    }

    /**
     * 分割 page
     */
    public static void splitPDF() throws Exception {

        FileOutputStream out = new FileOutputStream(FILE_DIR + "splitPDF.pdf");

        document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));

        document.newPage();
        document.add(new Paragraph(const_PAGE_FOUR, static_FONT_CHINESE));

        document.close();

        PdfReader reader = new PdfReader(FILE_DIR + "splitPDF.pdf");

        Document dd = new Document();
        PdfWriter writer = PdfWriter.getInstance(dd, new FileOutputStream(FILE_DIR + "splitPDF1.pdf"));
        dd.open();
        PdfContentByte cb = writer.getDirectContent();
        dd.newPage();
        cb.addTemplate(writer.getImportedPage(reader, 1), 0, 0);
        dd.newPage();
        cb.addTemplate(writer.getImportedPage(reader, 2), 0, 0);
        dd.close();
        writer.close();

        Document dd2 = new Document();
        PdfWriter writer2 = PdfWriter.getInstance(dd2, new FileOutputStream(FILE_DIR + "splitPDF2.pdf"));
        dd2.open();
        PdfContentByte cb2 = writer2.getDirectContent();
        dd2.newPage();
        cb2.addTemplate(writer2.getImportedPage(reader, 3), 0, 0);
        dd2.newPage();
        cb2.addTemplate(writer2.getImportedPage(reader, 4), 0, 0);
        dd2.close();
        writer2.close();
    }

    /**
     * 合并 PDF 文件
     */
    public static void mergePDF() throws Exception {

        PdfReader reader1 = new PdfReader(FILE_DIR + "splitPDF1.pdf");
        PdfReader reader2 = new PdfReader(FILE_DIR + "splitPDF2.pdf");

        FileOutputStream out = new FileOutputStream(FILE_DIR + "mergePDF.pdf");

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();
        PdfContentByte cb = writer.getDirectContent();

        java.util.List<PdfReader> readers = new ArrayList<PdfReader>();
        readers.add(reader1);
        readers.add(reader2);

        int pageOfCurrentReaderPDF = 0;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();

            // 在目标中为每个源页面创建一个新页面
            while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                document.newPage();
                pageOfCurrentReaderPDF++;
                PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                cb.addTemplate(page, 0, 0);
            }
            pageOfCurrentReaderPDF = 0;
        }
        out.flush();
        document.close();
        out.close();
    }

    /**
     * 排序page
     */
    public static void sortpage() throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "sortpage.pdf");

        document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setLinearPageMode();

        document.open();
        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_FOUR, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_FIVE, static_FONT_CHINESE));

        int[] order = { 4, 3, 2, 1 };
        writer.reorderPages(order);

        document.close();
    }

    /**
     * 页眉页脚
     */
    public static void setHeaderFooter() throws Exception {
        document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "setHeaderFooter.pdf"));

        writer.setPageEvent(new PdfPageEventHelper() {

            public void onEndPage(PdfWriter writer, Document document) {

                PdfContentByte cb = writer.getDirectContent();
                cb.saveState();

                cb.beginText();
                BaseFont bf = null;
                try {
                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cb.setFontAndSize(bf, 10);

                // Header
                float x = document.top(-20);

                // 左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "H-Left", document.left(), x, 0);
                // 中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, writer.getPageNumber() + " page", (document.right() + document.left()) / 2, x, 0);
                // 右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "H-Right", document.right(), x, 0);

                // Footer
                float y = document.bottom(-20);

                // 左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "F-Left", document.left(), y, 0);
                // 中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, writer.getPageNumber() + " page", (document.right() + document.left()) / 2, y, 0);
                // 右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "F-Right", document.right(), y, 0);

                cb.endText();

                cb.restoreState();
            }
        });

        document.open();
        document.add(new Paragraph("1 page"));

        document.newPage();
        document.add(new Paragraph("2 page"));

        document.newPage();
        document.add(new Paragraph("3 page"));

        document.newPage();
        document.add(new Paragraph("4 page"));

        document.close();
    }

    public static void addColumnText() throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "addColumnText.pdf");

        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        Phrase phrase1 = new Phrase(const_BIBIDONG + " is a beauty!left", static_FONT_CHINESE);
        Phrase phrase2 = new Phrase(const_BIBIDONG + " is a beauty!right", static_FONT_CHINESE);
        Phrase phrase3 = new Phrase(const_BIBIDONG + " is a beauty!center", static_FONT_CHINESE);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase1, 200, 700, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase2, 200, 600, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase3, 200, 500, 0);

        document.close();
    }

    /**
     * 文档视图
     */
    public static void setView() throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "setView.pdf");

        document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, out);

        writer.setPdfVersion(PdfWriter.VERSION_1_5);

        writer.setViewerPreferences(PdfWriter.PageModeFullScreen);// 全屏
        writer.setPageEvent(new PdfPageEventHelper() {
            public void onStartPage(PdfWriter writer, Document document) {
                writer.setTransition(new PdfTransition(PdfTransition.DISSOLVE, 3));
                writer.setDuration(5);// 间隔时间
            }
        });

        document.open();
        document.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_FOUR, static_FONT_CHINESE));
        document.newPage();
        document.add(new Paragraph(const_PAGE_FIVE, static_FONT_CHINESE));

        document.close();

    }

    /**
     * 压缩PDF到Zip
     */
    public static void pdfToZip() throws Exception {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(FILE_DIR + "pdfToZip.zip"));
        for (int i = 1; i <= 3; i++) {
            ZipEntry entry = new ZipEntry(const_NEZHA + i + ".pdf");
            zip.putNextEntry(entry);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, zip);
            writer.setCloseStream(false);
            document.open();
            document.add(new Paragraph(const_NEZHA + i, static_FONT_CHINESE));
            document.close();
            zip.closeEntry();
        }
        zip.close();
    }

    /**
     * 添加注释
     */
    public static void addAnnotation() throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "addAnnotation.pdf");
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        writer.setLinearPageMode();
        doc.open();
        doc.add(new Paragraph(const_PAGE_FIRST, static_FONT_CHINESE));
        doc.add(new Annotation("title", "this is a annotation!"));
        doc.newPage();
        doc.add(new Paragraph(const_PAGE_SECOND, static_FONT_CHINESE));
        Chunk chunk = new Chunk(const_NEZHA);
        chunk.setAnnotation(PdfAnnotation.createText(writer, null, "Title", "this is a another annotation!", false, "Comment"));
        doc.add(chunk);
        // 添加附件
        //doc.newPage();
        //doc.add(new Paragraph(const_PAGE_THIRD, static_FONT_CHINESE));
        //Chunk chunk2 = new Chunk(const_BIBIDONG, static_FONT_CHINESE);
        //PdfAnnotation annotation = PdfAnnotation.createFileAttachment(writer, null, "Title", null, const_JPG_JAVA, const_JPG_NGINX);
        //annotation.put(PdfName.NAME, new PdfString("Paperclip"));
        //chunk2.setAnnotation(annotation);
        //doc.add(chunk2);
        doc.close();
    }
}