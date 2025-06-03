package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        int[] intArray0 = byteOrderMark0.getRawBytes();
        ByteOrderMark byteOrderMark1 = new ByteOrderMark("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", intArray0);
        boolean boolean0 = byteOrderMark1.matches(intArray0);
        assertTrue(boolean0);
    }
}
