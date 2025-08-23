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

public class OptionFormatter_ESTestTest3 extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Option option0 = new Option((String) null, (String) null, true, (String) null);
        OptionFormatter.Builder optionFormatter_Builder0 = OptionFormatter.builder();
        optionFormatter_Builder0.setOptArgSeparator("Deprecated");
        OptionFormatter optionFormatter0 = optionFormatter_Builder0.build(option0);
        String string0 = optionFormatter0.toSyntaxOption();
        assertEquals("[Deprecated<arg>]", string0);
    }
}
