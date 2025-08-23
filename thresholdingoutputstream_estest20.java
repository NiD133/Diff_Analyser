package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.OutputStream;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ThresholdingOutputStream_ESTestTest20 extends ThresholdingOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        ThresholdingOutputStream thresholdingOutputStream0 = new ThresholdingOutputStream(129);
        thresholdingOutputStream0.setByteCount((byte) 1);
        assertEquals(1L, thresholdingOutputStream0.getByteCount());
    }
}
