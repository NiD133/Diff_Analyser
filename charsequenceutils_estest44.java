package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest44 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        StringBuilder stringBuilder0 = new StringBuilder(stringBuffer0);
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder0, 1114122, 0);
        assertEquals((-1), int0);
    }
}
