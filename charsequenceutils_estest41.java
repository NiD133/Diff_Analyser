package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest41 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        StringBuilder stringBuilder1 = stringBuilder0.appendCodePoint(65536);
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder1, 65536, 0);
        assertEquals(0, int0);
    }
}
