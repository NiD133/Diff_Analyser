package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest67 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test66() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        StringBuilder stringBuilder0 = new StringBuilder(stringBuffer0);
        int int0 = CharSequenceUtils.indexOf(stringBuffer0, stringBuilder0, (-1231));
        assertEquals(0, int0);
    }
}
