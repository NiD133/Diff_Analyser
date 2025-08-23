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

public class OptionFormatter_ESTestTest6 extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Option option0 = new Option((String) null, (String) null, true, (String) null);
        OptionFormatter optionFormatter0 = OptionFormatter.from(option0);
        OptionFormatter.Builder optionFormatter_Builder0 = new OptionFormatter.Builder(optionFormatter0);
        BiFunction<OptionFormatter, Boolean, String> biFunction0 = (BiFunction<OptionFormatter, Boolean, String>) mock(BiFunction.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(biFunction0).apply(any(org.apache.commons.cli.help.OptionFormatter.class), anyBoolean());
        OptionFormatter.Builder optionFormatter_Builder1 = optionFormatter_Builder0.setSyntaxFormatFunction(biFunction0);
        OptionFormatter optionFormatter1 = optionFormatter_Builder1.build(option0);
        String string0 = optionFormatter1.toSyntaxOption();
        assertNull(string0);
    }
}
