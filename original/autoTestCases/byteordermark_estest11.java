package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        String string0 = byteOrderMark0.toString();
        assertEquals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", string0);
    }
}
