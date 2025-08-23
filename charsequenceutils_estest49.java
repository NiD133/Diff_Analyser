package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest49 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("', ss }either of tpp Map.Entry nor an Array");
        stringBuilder0.deleteCharAt(1);
        int int0 = CharSequenceUtils.lastIndexOf("', ss }either of tpp Map.Entry nor an Array", stringBuilder0, 1);
        assertEquals((-1), int0);
    }
}
