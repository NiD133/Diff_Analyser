package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest15 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Options options0 = new Options();
        Options options1 = options0.addRequiredOption("j", "", true, "j");
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray0 = Locale.getISOCountries();
        posixParser0.flatten(options1, stringArray0, true);
        posixParser0.burstToken("$--", true);
    }
}
