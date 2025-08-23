package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest65 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test64() throws Throwable {
        int int0 = CharSequenceUtils.indexOf("', is neither of type Map.Entry nor an Array", 84, 1041);
        assertEquals((-1), int0);
    }
}