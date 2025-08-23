package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest21 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        int int0 = CharSequenceUtils.lastIndexOf("', is neither of type Map.Entry nor an Array", 102, 102);
        assertEquals(15, int0);
    }
}
