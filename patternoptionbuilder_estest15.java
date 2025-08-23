package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest15 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Class<?> class0 = PatternOptionBuilder.getValueType('@');
        assertEquals("class java.lang.Object", class0.toString());
        assertNotNull(class0);
    }
}
