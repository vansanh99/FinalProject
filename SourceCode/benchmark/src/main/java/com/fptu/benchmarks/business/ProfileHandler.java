/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import com.fptu.benchmarks.beans.Audit;
import com.fptu.benchmarks.beans.Level;
import com.fptu.benchmarks.constant.Constants;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author sanh
 */
@Log4j2
public class ProfileHandler {

    public Audit proccessProfile(Audit audit) {
        audit.getChapters().forEach(chap -> {
            chap.getCategories().forEach(cat -> {
                cat.getGroups().forEach(group -> {
                    group.getReports().forEach(report -> {
                        String pathToCheck = report.getPath();
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
                    }
                    );
                });
            });
        });
        return audit;
    }

    public void LevelFilter(Audit audit, Level selLevel) {
        if (CollectionUtils.isNotEmpty(audit.getChapters())) {
            audit.getChapters().forEach(lc -> {
                if (CollectionUtils.isNotEmpty(lc.getCategories())) {
                    lc.getCategories().forEach(cat -> {
                        if (CollectionUtils.isNotEmpty(cat.getGroups())) {
                            cat.getGroups().forEach(gr -> {
                                if (CollectionUtils.isNotEmpty(gr.getReports())) {
                                    gr.getReports().forEach(re -> {
                                        if (StringUtils.containsIgnoreCase(re.getLevel(), selLevel.getId())) {
                                            re.setFinalLevel(selLevel.getId());
                                        }
                                    });
                                } else {
                                    //errror herea
                                }
                            });
                        }
                    });
                } else {
                    //return error herer...
                }
            });
        } else {
            //return errror herer
        }
    }
}
