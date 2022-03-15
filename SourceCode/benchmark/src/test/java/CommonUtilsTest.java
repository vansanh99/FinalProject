
import com.fptu.benchmarks.business.CommonUtils;
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
    void commandTest() {
        CommonUtils.runPipeCommand("""
                                   /bin/bash[,]awk -F: '{print $4}' /etc/passwd | while read -r gid; do
                                   if ! grep -E -q '^.*?:[^:]*:$gid:' /etc/group; then
                                   echo 'The group ID '$gid' does not exist in /etc/group'
                                   fi
                                   done""");
        
    }
}
