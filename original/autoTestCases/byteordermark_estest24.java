package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
        String string0 = byteOrderMark0.getCharsetName();
        assertEquals("UTF-16BE", string0);
    }
}
