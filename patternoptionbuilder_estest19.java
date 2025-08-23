package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest19 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Class<?> class0 = PatternOptionBuilder.getValueType('%');
        assertEquals(1025, class0.getModifiers());
        assertNotNull(class0);
    }
}
