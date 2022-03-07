/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author vansa
 */
public class Fragment {

    public static void errMsgUI(final JPanel frame, String message, String title) {
        JOptionPane.showMessageDialog(frame, message,
                title, JOptionPane.ERROR_MESSAGE);
    }
}
