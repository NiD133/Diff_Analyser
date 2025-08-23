package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest56 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test55() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("', is neither of type Map.Entry nor an Array");
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder0, "', is neither of type Map.Entry nor an Array", 1041);
        assertEquals(0, int0);
    }
}
