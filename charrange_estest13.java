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

public class CharRange_ESTestTest13 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        CharRange charRange0 = CharRange.isIn('8', 'A');
        String string0 = charRange0.toString();
        assertEquals("8-A", string0);
        assertNotNull(string0);
    }
}
