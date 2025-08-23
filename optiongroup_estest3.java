package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest3 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        Option option0 = new Option((String) null, "", false, "cHfx;>NW^}R|1DYvgS");
        optionGroup0.setSelected(option0);
        String string0 = optionGroup0.getSelected();
        assertEquals("", string0);
    }
}
