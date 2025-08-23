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

public class CharRange_ESTestTest36 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        CharRange charRange0 = CharRange.isIn('8', 'A');
        char char0 = charRange0.getEnd();
        assertEquals('8', charRange0.getStart());
        assertFalse(charRange0.isNegated());
        assertEquals('A', char0);
    }
}