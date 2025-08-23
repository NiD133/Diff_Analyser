package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest2 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        boolean boolean0 = CharSequenceUtils.regionMatches(stringBuffer0, false, 0, stringBuffer0, 6, 0);
        assertFalse(boolean0);
    }
}
