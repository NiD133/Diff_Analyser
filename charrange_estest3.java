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

public class CharRange_ESTestTest3 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        CharRange charRange0 = CharRange.is('P');
        CharRange charRange1 = CharRange.isIn(';', 'z');
        boolean boolean0 = charRange1.contains(charRange0);
        assertEquals('z', charRange1.getEnd());
        assertEquals('P', charRange0.getEnd());
        assertEquals(';', charRange1.getStart());
        assertEquals('P', charRange0.getStart());
        assertTrue(boolean0);
    }
}
