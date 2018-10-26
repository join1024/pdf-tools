package com.join.tools.pdf.core;

import com.itextpdf.text.*;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
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
import java.util.List;

/**
 * 中文不显示问题，参考：
 * http://doc.okbase.net/gridmix/archive/2857.html
 * http://blog.51cto.com/gridmix/1229585
 * http://www.cnblogs.com/joann/p/4292862.html
 *
 * @author Join 2018-10-25
 */
public class PdfEngine {

    private  FreemarkerEngine freemarkerEngine;

    public PdfEngine(String templatesRootDir) throws IOException {
        this.freemarkerEngine = new FreemarkerEngine(templatesRootDir);
    }

    private void htmlToPDF(String htmlString,OutputStream pdfOutputStream) throws IOException,
        DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document,pdfOutputStream);
        document.open();

        /*document.addAuthor("pdf作者");
        document.addCreator("pdf创建者");
        document.addSubject("pdf主题");
        document.addCreationDate();
        document.addTitle("pdf标题,可在html中指定title");*/

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

        //fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        //使用我们的字体提供器，并将其设置为unicode字体样式
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, pdfWriter)));

        XMLWorker worker = new XMLWorker(pipeline, true);;
        InputStream inputStream=new ByteArrayInputStream(htmlString.getBytes(CommonUtils.UTF_8));
        InputStreamReader reader= new InputStreamReader(inputStream, CommonUtils.UTF_8);
        XMLParser xmlParser = new XMLParser(worker);
        xmlParser.parse(reader);

        document.close();
        reader.close();
        inputStream.close();
        pdfOutputStream.flush();
        pdfOutputStream.close();

    }

    public void processPdf(String templateName,Object data,OutputStream pdfOutputStream)
        throws IOException, TemplateException, DocumentException {
        String htmlString=freemarkerEngine.process(templateName,data);
        this.htmlToPDF(htmlString,pdfOutputStream);
    }

}
