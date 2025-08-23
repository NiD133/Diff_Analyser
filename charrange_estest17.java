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

public class CharRange_ESTestTest17 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CharRange charRange0 = CharRange.isNot('=');
        CharRange charRange1 = CharRange.is('=');
        boolean boolean0 = charRange1.equals(charRange0);
        assertEquals('=', charRange1.getStart());
        assertEquals('=', charRange0.getEnd());
        assertFalse(boolean0);
        assertEquals('=', charRange0.getStart());
        assertEquals('=', charRange1.getEnd());
    }
}
