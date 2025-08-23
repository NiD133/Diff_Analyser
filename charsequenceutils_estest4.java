package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest4 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder0, 1114111, 1114111);
        assertEquals((-1), int0);
    }
}
