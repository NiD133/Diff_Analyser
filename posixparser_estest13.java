package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest13 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Options options0 = new Options();
        Options options1 = options0.addRequiredOption("q", "", false, "q");
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray0 = Locale.getISOCountries();
        posixParser0.flatten(options1, stringArray0, false);
        posixParser0.burstToken("--", false);
    }
}
