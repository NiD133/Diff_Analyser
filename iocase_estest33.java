package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest33 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        boolean boolean0 = iOCase0.checkEquals("krmipp?jw", "1bq26gL^");
        assertFalse(boolean0);
    }
}
