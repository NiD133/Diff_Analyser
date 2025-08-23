package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest1 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        PosixParser posixParser0 = new PosixParser();
        posixParser0.burstToken("", true);
    }
}
