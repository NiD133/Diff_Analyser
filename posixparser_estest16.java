package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest16 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        PosixParser posixParser0 = new PosixParser();
        // Undeclared exception!
        try {
            posixParser0.burstToken(";-", true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.cli.PosixParser", e);
        }
    }
}
