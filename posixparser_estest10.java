package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PosixParser_ESTestTest10 extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Options options0 = new Options();
        String[] stringArray0 = Locale.getISOCountries();
        PosixParser posixParser0 = new PosixParser();
        String[] stringArray1 = posixParser0.flatten(options0, stringArray0, true);
        String[] stringArray2 = posixParser0.flatten(options0, stringArray1, true);
        assertEquals(252, stringArray2.length);
        assertEquals(251, stringArray1.length);
    }
}
