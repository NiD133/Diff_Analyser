package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest43 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        StringBuilder stringBuilder1 = stringBuilder0.appendCodePoint(65571);
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder1, 65541, 0);
        assertEquals((-1), int0);
    }
}