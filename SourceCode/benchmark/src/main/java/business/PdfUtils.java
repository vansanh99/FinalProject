/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import org.thymeleaf.context.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

/**
 *
 * @author vansa
 */
public class PdfUtils {

    /**
     * generate pdf from template file string
     *
     * @param context
     * @param templateFileName
     * @param pdfFile
     * @return
     * @throws IOException
     */
    public static File generatePdfFromHtml(Context context, String templateFileName, String pdfFile) throws IOException {
        File file = new File(pdfFile);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            // create pdf file
            //ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(generateHtmlStr(templateFileName, context), outputStream);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return file;
    }

    public static String generateHtmlStr(String templateFileName, Context context) {
        TemplateEngine templateEngine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setCacheable(false);
        resolver.setPrefix(CommonUtils.getConfigValue("templateFolder"));
        resolver.setSuffix(CommonUtils.getConfigValue("templateSuffix"));
        resolver.setCharacterEncoding(CommonUtils.getConfigValue("templateCharset"));
        resolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(resolver);
        return templateEngine.process(templateFileName, (context == null ? new Context() : context));
    }
}
