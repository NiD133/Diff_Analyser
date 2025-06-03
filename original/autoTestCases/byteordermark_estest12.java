package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
        int[] intArray0 = new int[3];
        boolean boolean0 = byteOrderMark0.matches(intArray0);
        assertFalse(boolean0);
    }
}
