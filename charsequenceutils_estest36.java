package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest36 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("', is either of typ Map.Entry nor anArray");
        boolean boolean0 = CharSequenceUtils.regionMatches(stringBuilder0, false, 1, "', is either of typ Map.Entry nor anArray", 0, 1);
        assertFalse(boolean0);
    }
}
