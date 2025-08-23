package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest4 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Options options0 = new Options();
        Options options1 = options0.addRequiredOption("j", "org.apache.commons.cli.PosixParser", true, "j");
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray0 = new String[9];
        stringArray0[2] = "-org.apache.commons.cli.PosixParser";
        posixParser0.flatten(options1, stringArray0, false);
        posixParser0.burstToken(";-", true);
    }
}
