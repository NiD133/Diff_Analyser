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

public class CharRange_ESTestTest28 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        CharRange charRange0 = CharRange.isNot('*');
        boolean boolean0 = charRange0.contains(charRange0);
        assertTrue(charRange0.isNegated());
        assertEquals('*', charRange0.getStart());
        assertTrue(boolean0);
        assertEquals('*', charRange0.getEnd());
    }
}
