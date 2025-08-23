package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest3 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Options options0 = new Options();
        options0.addRequiredOption("j", "j", true, "j");
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray0 = new String[5];
        stringArray0[1] = "-j";
        String[] stringArray1 = posixParser0.flatten(options0, stringArray0, true);
        assertEquals(1, stringArray1.length);
    }
}
