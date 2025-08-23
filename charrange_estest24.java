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

public class CharRange_ESTestTest24 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        CharRange charRange0 = CharRange.isIn('w', 'w');
        CharRange charRange1 = CharRange.isNotIn('(', '2');
        boolean boolean0 = charRange1.contains(charRange0);
        assertTrue(boolean0);
        assertEquals('w', charRange0.getStart());
        assertEquals('w', charRange0.getEnd());
        assertEquals('2', charRange1.getEnd());
        assertEquals('(', charRange1.getStart());
    }
}
