package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PatternOptionBuilder_ESTestTest34 extends PatternOptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Object object0 = PatternOptionBuilder.getValueClass('8');
        assertNull(object0);
    }
}
