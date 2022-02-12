/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import java.util.regex.Pattern;

/**
 *
 * @author vansa
 */
public class CommonUtils {
    /**
    * This method will return the result of matching regex.
    * 
    * @param text
    *            - Input String text
    * @param pattern
    *            - Input String patter
    * @return boolean - the result
    */
   public static boolean ismatchPattern(String text, String pattern) {
           return Pattern.compile(pattern).matcher(text).matches();
   }
}
