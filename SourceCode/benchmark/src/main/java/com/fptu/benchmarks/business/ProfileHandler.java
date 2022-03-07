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
                                List<Boolean> statuss = new ArrayList<>();
                                report.getCommandList().stream().forEach(cmd -> {
                                    log.debug("command: {}, type: {}, expect: {}", cmd.getCommand(), report.getType(), cmd.getExpectationPattern());
                                    String result = CommonUtils.runPipeCommand(cmd.getCommand());
                                    if (result != null) {
                                        if (CommonUtils.ismatchPattern(result, cmd.getExpectationPattern())) {
                                            statuss.add(Constants.TRUE);
                                        } else {
                                            statuss.add(Constants.FALSE);
                                        }
                                    }
                                });
                                log.debug("status: {}", statuss.toString());
                                for (Boolean s : statuss) {
                                    report.setStatus(s);
                                    if (StringUtils.equals(report.getOperator(), "OR")) {
                                        if (s) {
                                            report.setStatus(s);
                                            break;
                                        }
                                    }
                                    if (StringUtils.equals(report.getOperator(), "AND")) {
                                        if (!s) {
                                            report.setStatus(s);
                                            break;
                                        }
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
