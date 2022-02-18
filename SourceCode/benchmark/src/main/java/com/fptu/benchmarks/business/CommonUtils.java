/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 *
 * @author vansa
 */
public class CommonUtils {

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
        File fileConfig = new File("cfg.properties");
        String val = "";
        try {
            Properties props;
            try (FileReader reader = new FileReader(fileConfig)) {
                props = new Properties();
                props.load(reader);
            }
            val = props.getProperty(key);
        } catch (IOException ex) {
        }
        return val;
    }

}
