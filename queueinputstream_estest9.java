package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.commons.io.output.QueueOutputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueueInputStream_ESTestTest9 extends QueueInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        QueueInputStream queueInputStream0 = new QueueInputStream();
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            queueInputStream0.read(byteArray0, 2005, 2005);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Range [2005, 2005 + 2005) out of bounds for length 0
            //
            verifyException("org.apache.commons.io.input.QueueInputStream", e);
        }
    }
}
