package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest8 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        StringBuilder stringBuilder0 = new StringBuilder(stringBuffer0);
        stringBuilder0.insert(0, "6KY>-,V");
        char[] charArray0 = new char[20];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        int int0 = CharSequenceUtils.lastIndexOf(charBuffer0, stringBuilder0, 2750);
        assertEquals((-1), int0);
    }
}
