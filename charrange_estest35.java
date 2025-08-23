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

public class CharRange_ESTestTest35 extends CharRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        CharRange charRange0 = CharRange.isIn('8', 'A');
        assertFalse(charRange0.isNegated());
        Consumer<Character> consumer0 = (Consumer<Character>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        charRange0.forEach(consumer0);
        assertEquals('A', charRange0.getEnd());
        assertEquals('8', charRange0.getStart());
    }
}
