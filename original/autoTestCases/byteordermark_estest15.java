package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        int[] intArray0 = byteOrderMark0.getRawBytes();
        boolean boolean0 = byteOrderMark0.matches(intArray0);
        assertTrue(boolean0);
    }
}
