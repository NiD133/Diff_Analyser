package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest62 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        StringBuilder stringBuilder1 = stringBuilder0.appendCodePoint(0);
        int int0 = CharSequenceUtils.indexOf(stringBuilder1, 0, 0);
        assertEquals(0, int0);
    }
}
