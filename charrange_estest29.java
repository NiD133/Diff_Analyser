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

public class CharRange_ESTestTest29 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        CharRange charRange0 = CharRange.isIn('8', 'A');
        CharRange charRange1 = CharRange.isNotIn('Y', '@');
        boolean boolean0 = charRange1.contains(charRange0);
        assertEquals('8', charRange0.getStart());
        assertEquals('A', charRange0.getEnd());
        assertTrue(charRange1.isNegated());
        assertEquals('Y', charRange1.getEnd());
        assertFalse(boolean0);
        assertEquals('@', charRange1.getStart());
    }
}
