
import com.fptu.benchmarks.business.CommonUtils;
import com.fptu.benchmarks.model.Command;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sanh
 */
public class CommonUtilsTest {
    @Test
    void awkColumn() {
        InputStream in = new ByteArrayInputStream("1 23 5\n2 4 6".getBytes(StandardCharsets.UTF_8));
        try {
            String inp = CommonUtils.awkColumn(in,3);
            //assertEquals(new String(in.readAllBytes(), StandardCharsets.UTF_8),"5\n6");
            Command command = Command.builder().cmd("systemctl").args(new String[]{"is-enabled"}).build();
                CommonUtils.runCommand(Lists.newArrayList(command));
            System.out.println("sanh 1" + command.getCmd());
            if(command.getCmd().equals("awk")) {
                Optional<String> ar = Arrays.asList(command.getArgs()).stream()
                        .filter(a -> a.contains("print"))
                        .findFirst();
                System.out.println("sanh " + ar.get());
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CommonUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    void commandTest() {
        //System.out.println(CommonUtils.runPipeCommand(
          //      "grep,^banner\\-message\\-enable=true,/etc/gdm3/greeter.dconf-defaults"));
          System.out.println(StringUtils.contains("abcd", "abcde"));
    }
}
