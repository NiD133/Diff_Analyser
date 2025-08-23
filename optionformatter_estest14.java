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

public class OptionFormatter_ESTestTest14 extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        OptionFormatter optionFormatter0 = OptionFormatter.from((Option) null);
        // Undeclared exception!
        try {
            optionFormatter0.getLongOpt();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }
    }
}
