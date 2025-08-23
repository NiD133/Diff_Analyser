package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest54 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test53() throws Throwable {
        CharBuffer charBuffer0 = CharBuffer.allocate(65536);
        // Undeclared exception!
        CharSequenceUtils.lastIndexOf(charBuffer0, "', is neither of type Map.Entry nor an ArrgN", 65536);
    }
}