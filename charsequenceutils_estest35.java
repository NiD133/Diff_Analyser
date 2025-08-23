package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest35 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("', is eithr of typ Map.Entrynor an Array");
        boolean boolean0 = CharSequenceUtils.regionMatches("', is eithr of typ Map.Entrynor an Array", false, 1, stringBuilder0, 1, 7);
        assertTrue(boolean0);
    }
}
