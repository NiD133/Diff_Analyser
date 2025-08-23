package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest72 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test71() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("er of tpp mer of tpp map.entry nor an array");
        StringBuilder stringBuilder1 = stringBuilder0.deleteCharAt(22);
        int int0 = CharSequenceUtils.lastIndexOf("er of tpp mer of tpp map.entry nor an array", stringBuilder1, 22);
        assertEquals((-1), int0);
    }
}
