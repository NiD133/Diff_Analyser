package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class OptionGroup_ESTestTest11 extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        OptionGroup optionGroup0 = new OptionGroup();
        optionGroup0.setSelected((Option) null);
        assertFalse(optionGroup0.isRequired());
    }
}
