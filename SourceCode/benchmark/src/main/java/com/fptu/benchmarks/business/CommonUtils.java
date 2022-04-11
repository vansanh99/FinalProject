/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import com.fptu.benchmarks.constant.Constants;
import com.fptu.benchmarks.model.Command;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author vansa
 */
@Log4j2
public class CommonUtils {

    private static final File FILE_CONFIG = new File("cfg.properties");
    private static Properties props = loadProp();

    /**
     * This method will return the result of matching regex.
     *
     * @param text - Input String text
     * @param pattern - Input String patter
     * @return boolean - the result
     */
    public static boolean ismatchPattern(String text, String pattern) {
        return Pattern.compile(pattern).matcher(text).find();
    }

    public static String getConfigValue(String key) {
        String val = props.getProperty(key);
        return val;
    }

    public static Properties loadProp() {
        Properties props = null;
        try {
            try ( FileReader reader = new FileReader(FILE_CONFIG)) {
                props = new Properties();
                props.load(reader);
            }
        } catch (IOException ex) {
        }
        return props;
    }

    public static String runPipeCommand(String command) {
        log.info("command befor {}", command);
        String[] cmds = command.split(Constants.COMMAND_SEP);
        List<Command> commandLst = new ArrayList<>();
        for (int i = 0; i < cmds.length; i++) {
            log.info("command {}: {}", i, command);
            String cmd = cmds[i];
            String[] commandArr = cmd.split(Constants.PIPE);
            for (String c : commandArr) {
                //log.info("command in pipe {}", c);
                String[] a = c.split(Constants.COMMA_SEP);
                String[] args = new String[a.length - 1];
                if (a.length > 1) {
                    for (int j = 0; j < a.length - 1; j++) {
                        args[j] = a[j + 1];
                    }
                }
                Command com = Command.builder().cmd(a[0]).args(args).build();
                commandLst.add(com);
            }
        }
        String output = runCommand(commandLst);
        return output;
    }

    /**
     * Run command $cmd1 and pipe its STDOUT to $cmd2, just like executing:
     *
     * $ cmd1 | cmd2
     *
     * from a Linux shell
     *
     * @param commands
     * @return
     */
    public static String runCommand(List<Command> commands) {
        InputStream input = null;
        String finalOutput = "";
        for (Command command : commands) {
            log.info("cmd: {}, args: {}", command.getCmd(), command.getArgs());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            CommandLine commandLine = new CommandLine(command.getCmd()).addArguments(command.getArgs());
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(30000);
            executor.setWatchdog(watchdog);
            if (null == input) {
                log.info("inp null");
                executor.setStreamHandler(new PumpStreamHandler(output, error));
            } else {
                log.info("inp  not null");
                executor.setStreamHandler(new PumpStreamHandler(output, error, input));
            }
            try {
                executor.execute(commandLine);
                input = output.toInputStream();
                finalOutput = output.toString(StandardCharsets.UTF_8);
                log.info("success {}", finalOutput);
            } catch (IOException ex) {
                log.error("could not run command: {}", ex);
                finalOutput = error.toString(StandardCharsets.UTF_8);

            }
        }
        log.info("final output {}", finalOutput);
        return finalOutput;

    }
}
