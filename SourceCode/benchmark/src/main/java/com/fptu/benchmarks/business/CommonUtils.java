/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author vansa
 */
@Log4j2
public class CommonUtils {

    private static final File FILE_CONFIG= new File("cfg.properties");
    /**
     * This method will return the result of matching regex.
     *
     * @param text - Input String text
     * @param pattern - Input String patter
     * @return boolean - the result
     */
    public static boolean ismatchPattern(String text, String pattern) {
        return Pattern.compile(pattern).matcher(text).matches();
    }

    public static String getConfigValue(String key) {
        String val = "";
        try {
            Properties props;
            try (FileReader reader = new FileReader(FILE_CONFIG)) {
                props = new Properties();
                props.load(reader);
            }
            val = props.getProperty(key);
        } catch (IOException ex) {
        }
        return val;
    }

    public static String runCommand(String... command) {
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
            log.info("error {}", e);
        }
        return output;
    }
}
