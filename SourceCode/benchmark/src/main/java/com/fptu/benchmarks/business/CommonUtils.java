/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.business;

import com.fptu.benchmarks.model.Command;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            try (FileReader reader = new FileReader(FILE_CONFIG)) {
                props = new Properties();
                props.load(reader);
            }
        } catch (IOException ex) {
        }
        return props;
    }

    public static String runPipeCommand(String command) {
        log.info("command befor {}", command);
        String[] cmds = command.split(";");
        String output = "";
        /*try {
        InputStream input = null;
        OutputStream outputS = null;*/
        for (int i = 0; i < cmds.length; i++) {
            log.info("command {}: {}", i, command);
            String cmd = cmds[i];
            List<Command> commandLst = new ArrayList<>();
            String[] commandArr = cmd.split("\\|");
            for (String c : commandArr) {
                //log.info("command in pipe {}", c);
                String[] a = c.split(",");
                String[] args = new String[a.length - 1];
                if (a.length > 1) {
                    for (int j = 0; j < a.length - 1; j++) {
                        args[j] = a[j + 1];
                    }
                }
                Command com = Command.builder().cmd(a[0]).args(args).build();
                commandLst.add(com);
            }
            output = runCommand(commandLst);
            /*log.debug("command 1: {}", commandArr[0]);
                Process p1 = Runtime.getRuntime().exec(commandArr[0].split(","));
                final int exitValue = p1.waitFor();
                if (exitValue == 0) {
                    input = p1.getInputStream();
                    log.info("Successfully executed the command: " + command);
                } else {
                    try (final BufferedReader b = new BufferedReader(new InputStreamReader(p1.getErrorStream()))) {
                        String err = b.lines().collect(Collectors.joining(System.lineSeparator()));
                        log.info("error cmd {}", err);
                        return err;
                    } catch (final IOException e) {
                        log.error("error ioe {}", e);
                    }
                }
                if (commandArr.length > 1) {
                    for (int i = 1; i < commandArr.length; i++) {
                        String[] com = commandArr[i].split(",");
                        log.debug("command {}: {}", i, com);
                        Process p2 = Runtime.getRuntime().exec(com);
                        outputS = p2.getOutputStream();
                        IOUtils.copy(input, outputS);
                        input = p2.getInputStream();
                        outputS.close(); // signals grep to finish
                    }
                }*/
        }
        /*InputStreamReader inputStreamReader = new InputStreamReader(input);
            try ( BufferedReader rd = new BufferedReader(inputStreamReader)) {
                output = rd.lines().collect(Collectors.joining(System.lineSeparator()));
                log.info("command output: {}", output);
            }
        } catch (IOException e) {
            log.error("error1 {}", e);
        }
        /*catch (InterruptedException ex) {
            log.error("error2 {}", ex);
        }*/
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
            boolean runCmd = true;
            if (command.getCmd().equals("awk") && null != input) {
                log.info("cmd in awk last col: {}, args: {}", command.getCmd(), command.getArgs());
                Optional<String> ar = Arrays.asList(command.getArgs()).stream()
                        .filter(a -> a.contains("print"))
                        .findFirst();
                if (ar.isPresent()) {
                    String[] tks = ar.get().split("\\$");
                    int col = Integer.parseInt(tks[tks.length - 1]);
                    try {
                        finalOutput = awkColumn(input, col);
                    } catch (IOException ex) {
                        log.error("mot loi khac {}", ex);
                    }
                    input = new ByteArrayInputStream(finalOutput.getBytes(StandardCharsets.UTF_8));
                    runCmd = false;
                } else {
                    runCmd = true;
                }
            }
            if (runCmd) {
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
        }
        log.info("final output {}", finalOutput);
        return finalOutput;

    }

    public static String awkColumn(InputStream input, int col) throws IOException {
        String inputStr = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        String outStr = getLastColumnValue(inputStr, "\\s++", col);
        return outStr;
    }

    private static String getLastColumnValue(String value, String separator, int col) {
        //https://stackoverflow.com/questions/54630596/how-to-retrieve-last-column-from-set-of-data
        String[] lines = value.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String[] tokens = line.split(separator);
            //indexed from zero -> length -1
            if (tokens.length > 0 && col <= tokens.length) {
                if (!sb.isEmpty()) {
                    sb.append(System.getProperty("line.separator"));
                }
                sb.append(tokens[col - 1]);
            }
        }
        return sb.toString();
    }
}
