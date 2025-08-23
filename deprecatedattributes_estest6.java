package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DeprecatedAttributes_ESTestTest6 extends DeprecatedAttributes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test5() throws Throwable {
        DeprecatedAttributes deprecatedAttributes0 = DeprecatedAttributes.DEFAULT;
        String string0 = deprecatedAttributes0.getDescription();
        assertEquals("", string0);
    }
}
