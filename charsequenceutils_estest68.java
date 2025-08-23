package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest68 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test67() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("', is neither of type Map.Entry nor an Array");
        char[] charArray0 = new char[5];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        int int0 = CharSequenceUtils.indexOf(charBuffer0, stringBuilder0, (-1231));
        assertEquals((-1), int0);
    }
}
