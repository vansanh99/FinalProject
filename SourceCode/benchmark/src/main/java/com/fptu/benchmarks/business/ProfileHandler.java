/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import com.fptu.benchmarks.beans.Audit;
import com.fptu.benchmarks.beans.Category;
import com.fptu.benchmarks.beans.Chapter;
import com.fptu.benchmarks.beans.Group;
import com.fptu.benchmarks.beans.Level;
import com.fptu.benchmarks.beans.Report;
import com.fptu.benchmarks.constant.Constants;
import com.fptu.benchmarks.model.ProfileDetails;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author sanh
 */
@Log4j2
public class ProfileHandler {

    public Audit proccessProfile(Audit audit, JTable tableResult) {
        int index = 0;
        for (Chapter chapter : audit.getChapters()) {
            for (Category category : chapter.getCategories()) {
                for (Group group : category.getGroups()) {
                    for (Report report : group.getReports()) {
                        if (StringUtils.equals(ProfileDetails.getProfileLevel().getId(), report.getFinalLevel())) {
                            String pathToCheck = report.getPath();
                            DefaultTableModel model = (DefaultTableModel) tableResult.getModel();
                            String[] row = new String[4];
                            row[0] = ++index + "/" + ProfileDetails.getTotalItem();
                            row[1] = report.getDescription();
                            long t1 = System.currentTimeMillis();
                            switch (report.getType()) {
                                case Constants.FILE_EXISTENCE -> {//check file exists
                                    log.debug("file path: {}, type: {}, required: {}", pathToCheck, report.getType(), report.isRequired());
                                    File tempFile = new File(pathToCheck);
                                    report.setStatus(tempFile.exists() == report.isRequired());
                                    log.debug("status: {}", report.isStatus());
                                }
                                case Constants.FILE_PATTERN -> {//check file pattern
                                    log.debug("file path: {}, type: {}, file Pattrn: {}", pathToCheck, report.getType(), report.getPattern());
                                    String filePattern = report.getPattern();
                                    try {
                                        String fileString = new String(Files.readAllBytes(Paths.get(pathToCheck)), StandardCharsets.UTF_8);
                                        if (CommonUtils.ismatchPattern(fileString, filePattern)) {
                                            report.setStatus(Constants.TRUE);
                                        } else {
                                            report.setStatus(Constants.FALSE);
                                        }
                                    } catch (IOException ex) {
                                        report.setStatus(Constants.FALSE);
                                        log.debug("file not exists type 2");
                                    }
                                    log.debug("status: {}", report.isStatus());
                                }
                                case Constants.SHELL_RUN -> {//run shell
                                    log.info("operator bef: {}", report.getOperator());
                                    report.getCommandList().stream().forEach(cmd -> {
                                        log.debug("command: {}, type: {}, expect: {}", cmd.getCommand(), report.getType(), cmd.getExpectationPattern());
                                        String result = CommonUtils.runPipeCommand(cmd.getCommand());
                                        if (result != null) {
                                            if (CommonUtils.ismatchPattern(result, cmd.getExpectationPattern())) {
                                                if (report.getCommandList().size() > 1) {
                                                    report.setOperator(StringUtils.replace(report.getOperator(), cmd.getId(), "true"));
                                                } else {
                                                    report.setStatus(Constants.TRUE);
                                                }
                                            } else {
                                                if (report.getCommandList().size() > 1) {
                                                    report.setOperator(StringUtils.replace(report.getOperator(), cmd.getId(), "false"));
                                                } else {
                                                    report.setStatus(Constants.FALSE);
                                                }
                                            }
                                        }
                                    });
                                    if (report.getCommandList().size() > 1) {
                                        if (StringUtils.isNoneEmpty(report.getOperator())) {
                                            String languageName = "ECMAScript";
                                            String languageVersion = "ECMAScript 262 Edition 11";
                                            ScriptEngineManager manager = new ScriptEngineManager();
                                            List<ScriptEngineFactory> factories = manager.getEngineFactories();

                                            ScriptEngine engine = null;
                                            for (ScriptEngineFactory factory : factories) {
                                                String language = factory.getLanguageName();
                                                String version = factory.getLanguageVersion();

                                                if (language.equals(languageName)
                                                        && version.equals(languageVersion)) {
                                                    engine = factory.getScriptEngine();
                                                    break;
                                                }
                                            }

                                            if (engine != null) {
                                                try {
                                                    report.setStatus(((Boolean) engine.eval(report.getOperator())));
                                                } catch (ScriptException e) {
                                                    log.error("Scripte err {}", e);
                                                }
                                            }
                                            log.info("operator after: {}, status {}", report.getOperator(), report.isStatus());
                                        } else {
                                            log.error("operator can not be empty");
                                        }
                                    }
                                }
                            }
                            long t2 = System.currentTimeMillis();
                            row[2] = String.valueOf((t2 - t1) / 1000) + " second";
                            row[3] = String.valueOf(StringUtils.equals(report.getType(), "UNKNOWN") ? "Unknown" : (report.isStatus() ? "Pass" : "Fail"));
                            model.addRow(new Object[]{row[0], row[1], row[2], row[3]});
                            tableResult.setModel(model);
                        }
                    }
                }
            }
        }
        return audit;
    }

    public void LevelFilter(Audit audit, Level selLevel) {
        int index = 0;
        if (CollectionUtils.isNotEmpty(audit.getChapters())) {
            for (Chapter chapter : audit.getChapters()) {
                if (CollectionUtils.isNotEmpty(chapter.getCategories())) {
                    for (Category category : chapter.getCategories()) {
                        if (CollectionUtils.isNotEmpty(category.getGroups())) {
                            for (Group group : category.getGroups()) {
                                if (CollectionUtils.isNotEmpty(group.getReports())) {
                                    for (Report report : group.getReports()) {
                                        if (StringUtils.containsIgnoreCase(report.getLevel(), selLevel.getId())) {
                                            report.setFinalLevel(selLevel.getId());
                                            index++;
                                        }
                                    }
                                } else {
                                    //errror herea
                                }
                            }
                        }
                    }
                } else {
                    //return error herer...
                }
            }
            ProfileDetails.setTotalItem(index);
        } else {
            //return errror herer
        }
    }
}
