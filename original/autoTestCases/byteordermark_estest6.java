package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32LE;
        int int0 = byteOrderMark0.get(1);
        assertEquals(254, int0);
    }
}
