/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author sanh
 */
@Log4j2
public class ShellHandler {
    public String runCommand(String... command) {
    ProcessBuilder processBuilder = new ProcessBuilder().command(command);
    String output = null;
    try {
        Process process = processBuilder.start();
 
        //read the output
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        try (BufferedReader rd = new BufferedReader(inputStreamReader)) {
            output = rd.lines().collect(Collectors.joining());
            log.debug("command output: {}", output);
            process.waitFor();
            //close the resources
        }
        process.destroy();
 
    } catch (IOException | InterruptedException e) {
        log.info(e);
    }
    return output;
}
}
