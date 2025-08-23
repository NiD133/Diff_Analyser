package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest8 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        Option option0 = new Option((String) null, (String) null);
        OptionGroup optionGroup1 = optionGroup0.addOption(option0);
        Option option1 = new Option("vN", "", false, "");
        optionGroup1.addOption(option1);
        String string0 = optionGroup0.toString();
        assertEquals("[--null, -vN ]", string0);
    }
}
