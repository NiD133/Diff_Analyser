package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest48 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder("er of tpp mer of tpp map.entry nor an array");
        stringBuilder0.delete(22, 789);
        int int0 = CharSequenceUtils.lastIndexOf("er of tpp mer of tpp map.entry nor an array", stringBuilder0, 22);
        assertEquals(0, int0);
    }
}
