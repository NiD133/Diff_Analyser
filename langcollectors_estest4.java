package org.apache.commons.lang3.stream;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.function.Function;
import java.util.stream.Collector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LangCollectors_ESTestTest4 extends LangCollectors_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        // Undeclared exception!
        try {
            LangCollectors.collect((Collector<? super CharBuffer, CharBuffer, CharBuffer>) null, (CharBuffer[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.Objects", e);
        }
    }
}
