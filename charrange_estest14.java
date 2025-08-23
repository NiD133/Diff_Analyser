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

public class CharRange_ESTestTest14 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        CharRange charRange0 = CharRange.isNotIn('N', 'N');
        charRange0.hashCode();
        assertEquals('N', charRange0.getStart());
        assertEquals('N', charRange0.getEnd());
        assertTrue(charRange0.isNegated());
    }
}
