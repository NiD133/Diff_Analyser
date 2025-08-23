package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest10 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("");
        StringBuilder stringBuilder1 = stringBuilder0.append(1937);
        stringBuilder0.append(true);
        CharBuffer charBuffer0 = CharBuffer.wrap((CharSequence) stringBuilder1);
        StringBuilder stringBuilder2 = stringBuilder1.insert(0, (CharSequence) charBuffer0);
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder1, stringBuilder2, 0);
        assertEquals(0, int0);
    }
}
