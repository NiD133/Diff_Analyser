package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest6 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Options options0 = new Options();
        Options options1 = options0.addRequiredOption("j", "j", false, "j");
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray0 = new String[11];
        stringArray0[5] = "--=<iy";
        String[] stringArray1 = posixParser0.flatten(options1, stringArray0, false);
        String[] stringArray2 = posixParser0.flatten(options1, stringArray1, true);
        assertEquals(3, stringArray2.length);
    }
}
