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

public class OptionFormatter_ESTestTest1 extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Option option0 = new Option("?Wf", "?Wf", true, "?Wf");
        OptionFormatter.Builder optionFormatter_Builder0 = OptionFormatter.builder();
        OptionFormatter.Builder optionFormatter_Builder1 = optionFormatter_Builder0.setLongOptPrefix("?Wf");
        OptionFormatter optionFormatter0 = optionFormatter_Builder1.build(option0);
        String string0 = optionFormatter0.getLongOpt();
        assertEquals("?Wf?Wf", string0);
    }
}
