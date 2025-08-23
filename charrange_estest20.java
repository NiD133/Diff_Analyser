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

public class CharRange_ESTestTest20 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        CharRange charRange0 = CharRange.isIn('8', 'A');
        Object object0 = new Object();
        boolean boolean0 = charRange0.equals(object0);
        assertFalse(boolean0);
        assertEquals('8', charRange0.getStart());
        assertEquals('A', charRange0.getEnd());
    }
}
