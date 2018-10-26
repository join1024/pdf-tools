package com.join.tools.pdf;

import com.itextpdf.text.DocumentException;
import com.join.tools.pdf.core.FreemarkerEngine;
import com.join.tools.pdf.core.PdfEngine;
import com.join.tools.pdf.demo.Course;
import com.join.tools.pdf.demo.Student;
import com.join.tools.pdf.utils.CommonUtils;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Join 2018-10-25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FreemarkerEngineTest.class)
//@TestPropertySource("classpath:/application.properties")
public class FreemarkerEngineTest{

    @Autowired
    private ApplicationContext context;

    @Test
    public void test() throws IOException, TemplateException, DocumentException {
        String dir= CommonUtils.getRealPath("templates");
        PdfEngine engine=new PdfEngine(dir);
        List<Course> sourceList=new ArrayList<Course>();

        sourceList.add(new Course("English", (float) 98.9));
        sourceList.add(new Course("数学", (float) 99));
        Student student=new Student("张三",18, sourceList);
        FileOutputStream fileOutputStream=new FileOutputStream(new File( "D:/test.pdf"));
        engine.processPdf("test.html",student,fileOutputStream);
    }

}
