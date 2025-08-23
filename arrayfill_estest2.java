package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Arrays;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class ArrayFill_ESTestTest2 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        short[] shortArray0 = new short[6];
        short[] shortArray1 = ArrayFill.fill(shortArray0, (short) (-838));
        assertArrayEquals(new short[] { (short) (-838), (short) (-838), (short) (-838), (short) (-838), (short) (-838), (short) (-838) }, shortArray1);
    }
}
