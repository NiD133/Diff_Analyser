package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest23 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Class<Object> class0 = Object.class;
        OptionBuilder optionBuilder0 = OptionBuilder.withType(class0);
        assertNotNull(optionBuilder0);
    }
}
