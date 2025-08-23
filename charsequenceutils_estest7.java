package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest7 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        stringBuilder0.append((-1.0F));
        int int0 = CharSequenceUtils.lastIndexOf(stringBuilder0, (-778), 4);
        assertEquals((-1), int0);
    }
}
