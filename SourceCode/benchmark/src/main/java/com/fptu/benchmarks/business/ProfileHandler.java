/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import com.fptu.benchmarks.beans.Profile;
import com.fptu.benchmarks.constant.Constants;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author sanh
 */
@Log4j2
public class ProfileHandler {

    public Profile proccessProfile(Profile profile) {
        profile.getAudits().forEach(audit -> {
            if (audit.getLevel() == 1) {
                audit.getChapters().forEach(chap -> {
                    chap.getCategories().forEach(cat -> {
                        cat.getReports().forEach(report -> {
                            String pathToCheck = report.getPath();
                            log.debug("file path: " + pathToCheck + " type: " + report.getType());
                            switch (report.getType()) {
                                case Constants.FILE_EXISTENCE: {//check file exists
                                    File tempFile = new File(pathToCheck);
                                    report.setStatus(tempFile.exists());
                                    break;
                                }
                                case Constants.FILE_PATTERN: {//check file pattern
                                    String filePattern = report.getPattern();
                                    try {
                                        String fileString = new String(Files.readAllBytes(Paths.get(pathToCheck)), StandardCharsets.UTF_8);
                                        log.debug("fie:" + filePattern + " " + fileString);
                                        if (CommonUtils.ismatchPattern(fileString, filePattern)) {
                                            report.setStatus(Constants.TRUE);
                                        } else {
                                            report.setStatus(Constants.FALSE);
                                        }
                                    } catch (IOException ex) {
                                        report.setStatus(Constants.FALSE);
                                        log.debug("file not exists type 2");
                                    }
                                    break;
                                }
                                case Constants.SHELL_RUN: {//run shell
                                    ShellHandler sh = new ShellHandler();
                                    String result = sh.runCommand(report.getCommand().split(","));
                                    if (result != null) {
                                        if (CommonUtils.ismatchPattern(result, report.getExpectationPattern())) {
                                            report.setStatus(Constants.TRUE);
                                        } else {
                                            report.setStatus(Constants.FALSE);
                                        }
                                    }
                                    break;
                                }
                            }

                        });
                    });
                });
            }
        });
        return profile;
    }
}
