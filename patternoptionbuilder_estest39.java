package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest39 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Class<?> class0 = PatternOptionBuilder.getValueType('3');
        assertNull(class0);
    }
}
