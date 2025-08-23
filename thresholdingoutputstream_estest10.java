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

public class ThresholdingOutputStream_ESTestTest10 extends ThresholdingOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DeferredFileOutputStream.Builder deferredFileOutputStream_Builder0 = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream.Builder deferredFileOutputStream_Builder1 = deferredFileOutputStream_Builder0.setPrefix("/-f5");
        DeferredFileOutputStream deferredFileOutputStream0 = deferredFileOutputStream_Builder1.get();
        // Undeclared exception!
        try {
            deferredFileOutputStream0.write((byte[]) null, (-3053), 76);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid prefix or suffix
            //
            verifyException("java.nio.file.TempFileHelper", e);
        }
    }
}
