package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest4 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        CpioUtil.long2byteArray(3634, 3634, false);
        CpioUtil.long2byteArray(3634, 3634, false);
        // Undeclared exception!
        CpioUtil.long2byteArray(3634, 3634, false);
    }
}
