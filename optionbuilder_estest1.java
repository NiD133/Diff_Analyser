package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionBuilder_ESTestTest1 extends OptionBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Class<String> class0 = String.class;
        OptionBuilder optionBuilder0 = OptionBuilder.withType((Object) class0);
        assertNotNull(optionBuilder0);
    }
}
