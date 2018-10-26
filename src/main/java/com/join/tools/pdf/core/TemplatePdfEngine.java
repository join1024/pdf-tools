package com.join.tools.pdf.core;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.join.tools.pdf.utils.CommonUtils;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 中文不显示问题，参考：
 * http://doc.okbase.net/gridmix/archive/2857.html
 * http://blog.51cto.com/gridmix/1229585
 * http://www.cnblogs.com/joann/p/4292862.html
 *
 * @author Join 2018-10-25
 */
public class TemplatePdfEngine {

    private FreemarkerEngine freemarkerEngine;

    private CSSResolver cssResolver;

    private HtmlPipelineContext htmlContext;

    public TemplatePdfEngine(String templatesRootDir) throws IOException {
        this.freemarkerEngine = new FreemarkerEngine(templatesRootDir);
        init();
    }

    private void init(){
        //解决中文字体
        XMLWorkerFontProvider fontProvider=new XMLWorkerFontProvider(){
            @Override
            public Font getFont(final String fontname, String encoding, float size, final int style) {
                String fntname = fontname;
                if(fntname==null){
                    fntname="宋体";
                }
                return super.getFont(fntname, encoding, size, style);
            }

        };

        fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        //设置字体提供器，并设置为unicode字体样式
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
    }

    /**
     * html文档转换为pdf文档
     * @param htmlString
     * @param pdfOutputStream pdf输出流
     * @throws IOException
     * @throws DocumentException
     */
    private void htmlToPDF(String htmlString,OutputStream pdfOutputStream) throws IOException,
        DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document,pdfOutputStream);
        document.open();
        document.addCreationDate();

        /*document.addAuthor("作者");
        document.addCreator("创建者");
        document.addSubject("主题");
        document.addCreationDate();
        document.addTitle("pdf标题,可在html中指定title");*/

        PdfWriterPipeline pdfWriterPipeline=new PdfWriterPipeline(document, pdfWriter);
        HtmlPipeline htmlPipeline=new HtmlPipeline(htmlContext, pdfWriterPipeline);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

        XMLWorker xmlWorker = new XMLWorker(pipeline, true);
        InputStream inputStream=new ByteArrayInputStream(htmlString.getBytes(CommonUtils.UTF_8));
        InputStreamReader reader= new InputStreamReader(inputStream, CommonUtils.UTF_8);
        XMLParser xmlParser = new XMLParser(xmlWorker);
        xmlParser.parse(reader);
        xmlParser.flush();

        document.close();
        reader.close();
        inputStream.close();
        pdfOutputStream.flush();
        pdfOutputStream.close();
        xmlWorker.close();

    }

    private void htmlToPDF2(String htmlString,OutputStream pdfOutputStream) throws IOException,
        DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document,pdfOutputStream);
        document.open();
        document.addCreationDate();

        //解决中文字体
        XMLWorkerFontProvider fontProvider=new XMLWorkerFontProvider(){
            @Override
            public Font getFont(final String fontname, String encoding, float size, final int style) {

                Font FontChinese = null;
                try {
                    BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                        "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    FontChinese = new Font(bfChinese, 12, Font.NORMAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(FontChinese==null){
                    FontChinese = super.getFont(fontname, encoding, size, style);
                }
                return FontChinese;
            }

        };

        InputStream inputStream=new ByteArrayInputStream(htmlString.getBytes(CommonUtils.UTF_8));
        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, inputStream,Charset.forName(CommonUtils.UTF_8),fontProvider);

        document.close();
        pdfOutputStream.flush();
        pdfOutputStream.close();

    }

    private void htmlToPDF3(String htmlString,OutputStream pdfOutputStream) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlString, pdfOutputStream, converterProperties);
    }

    public void processPdf(String templateName,Object data,OutputStream pdfOutputStream)
        throws IOException, TemplateException, DocumentException {
        String htmlString=freemarkerEngine.process(templateName,data);
        this.htmlToPDF(htmlString,pdfOutputStream);
    }

}
