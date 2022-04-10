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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.RFonts;

/**
 *
 * @author vansa
 */
@Log4j2
public class ReportUtils {

    public static void generateReportFromHtml(Context context, String templateFileName, String outFolder, JTable tableResult) {
        log.info("geneate pdf from html starts");
        String reportNm = outFolder + "/" + System.getProperty("user.name") + "-" + System.getProperty("os.name")
                + "-" + System.getProperty("os.version") + "-" + System.currentTimeMillis();

        DefaultTableModel model = (DefaultTableModel) tableResult.getModel();
        String[] row = new String[4];
        row[0] = StringUtils.EMPTY;
        long time1 = System.currentTimeMillis();
        String htmlStr = generateHtmlStr(templateFileName, context);
        if ((boolean) context.getVariable("htmlReport")) {
            row[1] = "Generating html report...";
            try {

                log.info("start saving html");
                FileUtils.write(new File(reportNm + ".html"), htmlStr, StandardCharsets.UTF_8, false);
                log.info("save file html");
                row[3] = "Done";
            } catch (NullPointerException | ClassCastException e) {
                log.error("failed e {}", e);
                row[3] = "Fail";
            } catch (IOException ex) {
                log.error("failed ex {}", ex);
                row[3] = "Fail";
            }
            long time2 = System.currentTimeMillis();
            row[2] = String.valueOf((time2 - time1)/1000) + " second";
            model.addRow(new Object[]{row[0], row[1], row[2], row[3]});
            tableResult.setModel(model);
        }
        try {
            if ((boolean) context.getVariable("pdfReport")) {
                row[1] = "Generating Pdf report...";
                long t1 = System.currentTimeMillis();
                log.info("start saving pdf");
                File file = new File(reportNm + ".pdf");
                try ( OutputStream outputStream = new FileOutputStream(file)) {
                    // create pdf file
                    //ConverterProperties converterProperties = new ConverterProperties();
                    HtmlConverter.convertToPdf(htmlStr, outputStream);
                    log.info("saved file pdf");
                    row[3] = "Done";
                } catch (IOException e) {
                    log.error("failed {}", e);
                    row[3] = "Fail";
                }
                long t2 = System.currentTimeMillis();
                row[2] = String.valueOf((t2 - t1)/1000) + " second";
                model.addRow(new Object[]{row[0], row[1], row[2], row[3]});
                tableResult.setModel(model);
            }
            if ((boolean) context.getVariable("docxReport")) {
                row[1] = "Generating docx report...";
                long t1 = System.currentTimeMillis();
                log.info("generate docx...");
                generateDocxFromHtml(htmlStr, reportNm + ".docx");
                log.info("generate docx completed!");
                long t2 = System.currentTimeMillis();
                row[3] = "Done";
                row[2] = String.valueOf((t2 - t1)/1000) + " second";
                model.addRow(new Object[]{row[0], row[1], row[2], row[3]});
                tableResult.setModel(model);
            }
        } catch (NullPointerException | ClassCastException e) {
            log.error("failed {}", e);
        } catch (IOException | JAXBException | Docx4JException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * generate docx from template file string
     *
     * @param stringFromFile
     * @param filePath
     * @throws org.docx4j.openpackaging.exceptions.Docx4JException
     * @throws IOException
     * @throws javax.xml.bind.JAXBException
     */
    public static void generateDocxFromHtml(String stringFromFile, String filePath) throws Docx4JException, IOException, JAXBException {
        // Images: provide correct baseURL
        String baseURL = "file:///bvols/@git/repos/docx4j-ImportXHTML/sample-docs/docx/sample-docxv2.docx_files";
//        String baseURL = "file:///C:/Users/jharrop/git/docx4j-ImportXHTML/sample-docs/docx/sample-docxv2.docx_files";
        String unescaped = stringFromFile;
//        if (stringFromFile.contains("&lt;/") ) {
//    		unescaped = StringEscapeUtils.unescapeHtml(stringFromFile);        	
//        }

//        XHTMLImporter.setTableFormatting(FormattingOption.IGNORE_CLASS);
//        XHTMLImporter.setParagraphFormatting(FormattingOption.IGNORE_CLASS);
        //System.out.println("Unescaped: " + unescaped);
        // Setup font mapping
        RFonts rfonts = org.docx4j.jaxb.Context.getWmlObjectFactory().createRFonts();
        rfonts.setAscii("Century Gothic");
        XHTMLImporterImpl.addFontMapping("Century Gothic", rfonts);

        // Create an empty docx package
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(System.getProperty("user.dir") + "/styled.docx"));

        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
        ndp.unmarshalDefaultNumbering();

        // Convert the XHTML, and add it into the empty docx we made
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

        XHTMLImporter.setHyperlinkStyle("Hyperlink");
        wordMLPackage.getMainDocumentPart().getContent().addAll(
                XHTMLImporter.convert(unescaped, baseURL));

        //System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));
//		System.out.println(
//				XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getNumberingDefinitionsPart().getJaxbElement(), true, true));
        wordMLPackage.save(new java.io.File(filePath));
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
        log.info("geneate html string ends");
        return html;
    }
}
