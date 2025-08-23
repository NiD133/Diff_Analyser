package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DeprecatedAttributes_ESTestTest2 extends DeprecatedAttributes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        DeprecatedAttributes.Builder deprecatedAttributes_Builder0 = DeprecatedAttributes.builder();
        DeprecatedAttributes.Builder deprecatedAttributes_Builder1 = deprecatedAttributes_Builder0.setSince("+,ygu");
        DeprecatedAttributes deprecatedAttributes0 = deprecatedAttributes_Builder1.get();
        String string0 = deprecatedAttributes0.getSince();
        assertEquals("", deprecatedAttributes0.getDescription());
        assertFalse(deprecatedAttributes0.isForRemoval());
        assertEquals("+,ygu", string0);
    }
}
