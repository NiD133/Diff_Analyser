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

public class CharRange_ESTestTest16 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        CharRange charRange0 = CharRange.isNot('O');
        CharRange charRange1 = CharRange.isNot('O');
        boolean boolean0 = charRange1.equals(charRange0);
        assertEquals('O', charRange1.getEnd());
        assertEquals('O', charRange1.getStart());
        assertTrue(charRange1.isNegated());
        assertTrue(boolean0);
    }
}
