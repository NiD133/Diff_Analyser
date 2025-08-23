package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSet_ESTestTest4 extends CharSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        String[] stringArray0 = new String[3];
        CharSet charSet0 = new CharSet(stringArray0);
        CharRange[] charRangeArray0 = charSet0.getCharRanges();
        assertEquals(0, charRangeArray0.length);
    }
}
