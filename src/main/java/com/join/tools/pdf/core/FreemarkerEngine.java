package com.join.tools.pdf.core;

import com.join.tools.pdf.utils.CommonUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;

/**
 * freemarker参考手册：
 * https://freemarker.apache.org/docs/dgui_quickstart.html
 * @author Join 2018-10-25
 */
public class FreemarkerEngine {

    private Configuration configuration;

    /**
     * 启动时指定模板的根路径
     * @param templatesRootDir
     * @throws IOException
     */
    public FreemarkerEngine(String templatesRootDir) throws IOException {
        File file=new File(templatesRootDir);
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDirectoryForTemplateLoading(file);
        configuration.setDefaultEncoding(CommonUtils.UTF_8);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setClassicCompatible(true);
    }

    /**
     * 将数据填充到freemarker模板并生成html文档
     * @param templateFileName 要处理的模板文件路径（相对上面构造方法中的跟路径）
     * @param data 需要填充的数据
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String process(String templateFileName, Object data) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateFileName);
        StringWriter stringWriter = new StringWriter();
        template.process(data, stringWriter);
        return stringWriter.toString();
    }

}
