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

public class CharRange_ESTestTest1 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        CharRange charRange0 = CharRange.isIn('?', '?');
        CharRange charRange1 = CharRange.isIn('D', '?');
        boolean boolean0 = charRange1.equals(charRange0);
        assertEquals('D', charRange1.getEnd());
        assertFalse(charRange0.equals((Object) charRange1));
        assertEquals('?', charRange1.getStart());
        assertFalse(boolean0);
    }
}
