package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest22 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("', is neither of type Map.Entry nor an Array");
        StringBuffer stringBuffer0 = new StringBuffer(0);
        int int0 = CharSequenceUtils.indexOf(stringBuilder0, stringBuffer0, Integer.MAX_VALUE);
        assertEquals(44, int0);
    }
}
