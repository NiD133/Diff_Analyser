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

public class CharRange_ESTestTest26 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        CharRange charRange0 = CharRange.isNot('*');
        CharRange charRange1 = CharRange.isNot('m');
        boolean boolean0 = charRange1.contains(charRange0);
        assertEquals('m', charRange1.getStart());
        assertFalse(boolean0);
        assertTrue(charRange1.isNegated());
        assertEquals('m', charRange1.getEnd());
    }
}
