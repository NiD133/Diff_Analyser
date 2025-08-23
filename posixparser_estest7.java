package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest7 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Options options0 = new Options();
        Options options1 = options0.addRequiredOption("j", "j", false, "j");
        Options options2 = options1.addOption("j", "--WS", false, "--WS");
        String[] stringArray0 = new String[12];
        stringArray0[10] = "--=<ibn";
        PosixParser posixParser0 = new PosixParser();
        try {
            posixParser0.flatten(options2, stringArray0, false);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // Ambiguous option: '--'  (could be: 'j', '--WS')
            //
            verifyException("org.apache.commons.cli.PosixParser", e);
        }
    }
}
