package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest12 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        Option option0 = new Option("oQxw", (String) null, true, "[]");
        optionGroup0.setSelected(option0);
        boolean boolean0 = optionGroup0.isSelected();
        assertTrue(boolean0);
    }
}
