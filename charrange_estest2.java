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

public class CharRange_ESTestTest2 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        CharRange charRange0 = CharRange.isIn('+', '+');
        CharRange charRange1 = CharRange.isNotIn('7', '7');
        boolean boolean0 = charRange1.equals(charRange0);
        assertFalse(boolean0);
        assertEquals('7', charRange1.getStart());
        assertEquals('+', charRange0.getEnd());
        assertTrue(charRange1.isNegated());
        assertEquals('+', charRange0.getStart());
        assertEquals('7', charRange1.getEnd());
    }
}
