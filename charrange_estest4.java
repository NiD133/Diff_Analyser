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

public class CharRange_ESTestTest4 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        CharRange charRange0 = CharRange.isNot('*');
        CharRange charRange1 = CharRange.is('*');
        boolean boolean0 = charRange0.contains(charRange1);
        assertEquals('*', charRange1.getEnd());
        assertEquals('*', charRange0.getStart());
        assertEquals('*', charRange0.getEnd());
        assertFalse(boolean0);
        assertEquals('*', charRange1.getStart());
    }
}
