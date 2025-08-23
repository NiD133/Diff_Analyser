package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class CharRange_ESTestTest8 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        CharRange charRange0 = CharRange.isNot('}');
        char char0 = charRange0.getStart();
        assertTrue(charRange0.isNegated());
        assertEquals('}', char0);
        assertEquals('}', charRange0.getEnd());
    }
}
