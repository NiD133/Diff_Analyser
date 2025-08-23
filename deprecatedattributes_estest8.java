package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DeprecatedAttributes_ESTestTest8 extends DeprecatedAttributes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test7() throws Throwable {
        DeprecatedAttributes deprecatedAttributes0 = DeprecatedAttributes.DEFAULT;
        String string0 = deprecatedAttributes0.getSince();
        assertEquals("", string0);
    }
}
