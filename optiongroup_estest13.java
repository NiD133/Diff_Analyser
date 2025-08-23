package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest13 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        boolean boolean0 = optionGroup0.isSelected();
        assertFalse(boolean0);
    }
}
