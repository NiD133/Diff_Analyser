package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest15 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder(";i]b}Z<[yv");
        stringBuilder0.appendCodePoint(1114111);
        int int0 = CharSequenceUtils.indexOf(stringBuilder0, 65536, (-897));
        assertEquals((-1), int0);
    }
}
