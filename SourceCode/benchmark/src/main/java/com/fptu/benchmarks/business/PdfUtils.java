/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import org.thymeleaf.context.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import com.itextpdf.html2pdf.HtmlConverter;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author vansa
 */
@Log4j2
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
    public static File generatePdfFromHtml(Context context, String templateFileName, String pdfFile) {
        log.info("geneate pdf from html starts");
        String htmlStr = generateHtmlStr(templateFileName, context);
        try {
            if ((boolean) context.getVariable("pdfReport")) {
                log.info("start saving pdf");
                File file = new File(pdfFile);
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    // create pdf file
                    //ConverterProperties converterProperties = new ConverterProperties();
                    HtmlConverter.convertToPdf(htmlStr, outputStream);
                    log.info("saved file pdf");
                    return file;
                } catch (IOException e) {
                    log.error("failed {}", e);
                }
            }
        } catch (NullPointerException | ClassCastException e) {
            log.error("failed {}", e);
        }
        log.info("no pdf");
        return null;
    }

    public static String generateHtmlStr(String templateFileName, Context context) {
        log.info("geneate html string starts");
        TemplateEngine templateEngine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setCacheable(false);
        resolver.setPrefix(CommonUtils.getConfigValue("templateFolder"));
        resolver.setSuffix(CommonUtils.getConfigValue("templateSuffix"));
        resolver.setCharacterEncoding(CommonUtils.getConfigValue("templateCharset"));
        resolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(resolver);
        String html = templateEngine.process(templateFileName, (context == null ? new Context() : context));
        try {
            if ((boolean) context.getVariable("htmlReport")) {
                log.info("start saving html");
                FileUtils.write(new File("reports/test.html"), html, StandardCharsets.UTF_8, false);
                log.info("save file html");
            }
        } catch (NullPointerException | ClassCastException e) {
            log.error("failed {}", e);
        } catch (IOException ex) {
            log.error("error with file i/o {}", ex);
        }
        log.info("geneate html string ends");
        return html;
    }
}
