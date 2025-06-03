package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        int[] intArray0 = new int[4];
        intArray0[0] = (-8);
        ByteOrderMark byteOrderMark0 = new ByteOrderMark("+U", intArray0);
        int int0 = byteOrderMark0.get(0);
        assertEquals((-8), int0);
    }
}
