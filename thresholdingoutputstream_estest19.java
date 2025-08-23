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

public class ThresholdingOutputStream_ESTestTest19 extends ThresholdingOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        ThresholdingOutputStream thresholdingOutputStream0 = new ThresholdingOutputStream(1, (IOConsumer<ThresholdingOutputStream>) null, (IOFunction<ThresholdingOutputStream, OutputStream>) null);
        assertEquals(1, thresholdingOutputStream0.getThreshold());
    }
}
