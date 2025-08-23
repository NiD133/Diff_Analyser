package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DeprecatedAttributes_ESTestTest3 extends DeprecatedAttributes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        DeprecatedAttributes.Builder deprecatedAttributes_Builder0 = DeprecatedAttributes.builder();
        deprecatedAttributes_Builder0.setDescription("RSb&;Wj}.&C.b S?5");
        DeprecatedAttributes deprecatedAttributes0 = deprecatedAttributes_Builder0.get();
        String string0 = deprecatedAttributes0.getDescription();
        assertEquals("", deprecatedAttributes0.getSince());
        assertFalse(deprecatedAttributes0.isForRemoval());
        assertEquals("RSb&;Wj}.&C.b S?5", string0);
    }
}
