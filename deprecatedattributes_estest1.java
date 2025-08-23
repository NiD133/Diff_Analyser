package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DeprecatedAttributes_ESTestTest1 extends DeprecatedAttributes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        DeprecatedAttributes.Builder deprecatedAttributes_Builder0 = DeprecatedAttributes.builder();
        deprecatedAttributes_Builder0.setForRemoval(true);
        DeprecatedAttributes deprecatedAttributes0 = deprecatedAttributes_Builder0.get();
        boolean boolean0 = deprecatedAttributes0.isForRemoval();
        assertTrue(boolean0);
    }
}
