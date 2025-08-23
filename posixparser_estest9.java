package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest9 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Options options0 = new Options();
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray0 = new String[9];
        stringArray0[5] = "--=<q;n";
        String[] stringArray1 = posixParser0.flatten(options0, stringArray0, false);
        assertEquals(1, stringArray1.length);
    }
}
