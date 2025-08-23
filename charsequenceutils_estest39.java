package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest39 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        boolean boolean0 = CharSequenceUtils.regionMatches(stringBuilder0, true, 16, stringBuilder0, (-2908), 16);
        assertFalse(boolean0);
    }
}
