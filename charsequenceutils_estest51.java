package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest51 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        CharBuffer charBuffer0 = CharBuffer.allocate(3);
        int int0 = CharSequenceUtils.lastIndexOf(charBuffer0, charBuffer0, 3);
        assertEquals(0, int0);
    }
}
