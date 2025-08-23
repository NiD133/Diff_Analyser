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

public class OptionFormatter_ESTestTest25 extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Option option0 = new Option("?Wf", true, "?Wf");
        OptionFormatter.Builder optionFormatter_Builder0 = OptionFormatter.builder();
        BiFunction<OptionFormatter, Boolean, String> biFunction0 = (BiFunction<OptionFormatter, Boolean, String>) mock(BiFunction.class, new ViolatedAssumptionAnswer());
        optionFormatter_Builder0.setSyntaxFormatFunction(biFunction0);
        OptionFormatter optionFormatter0 = optionFormatter_Builder0.build(option0);
        assertFalse(optionFormatter0.isRequired());
    }
}
