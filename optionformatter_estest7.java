package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.BiFunction;
import org.apache.commons.cli.Option;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class OptionFormatter_ESTestTest7 extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Option option0 = new Option("?Wf", "?Wf", true, "?Wf");
        option0.setRequired(true);
        OptionFormatter optionFormatter0 = OptionFormatter.from(option0);
        boolean boolean0 = optionFormatter0.isRequired();
        assertTrue(boolean0);
    }
}
