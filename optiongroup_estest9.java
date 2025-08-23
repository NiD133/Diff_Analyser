package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest9 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        Option option0 = new Option("oQxw", (String) null, true, "[]");
        optionGroup0.setSelected(option0);
        Option option1 = new Option("Xr0g", false, "[");
        try {
            optionGroup0.setSelected(option1);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            //
            // The option 'Xr0g' was specified but an option from this group has already been selected: 'oQxw'
            //
            verifyException("org.apache.commons.cli.OptionGroup", e);
        }
    }
}
