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

public class ThresholdingOutputStream_ESTestTest9 extends ThresholdingOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DeferredFileOutputStream.Builder deferredFileOutputStream_Builder0 = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream deferredFileOutputStream0 = deferredFileOutputStream_Builder0.get();
        // Undeclared exception!
        try {
            deferredFileOutputStream0.write((byte[]) null, (-122), (-122));
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.output.ByteArrayOutputStream", e);
        }
    }
}
