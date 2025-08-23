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

public class QueueInputStream_ESTestTest8 extends QueueInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        PriorityBlockingQueue<Integer> priorityBlockingQueue0 = new PriorityBlockingQueue<Integer>();
        QueueInputStream queueInputStream0 = new QueueInputStream(priorityBlockingQueue0);
        byte[] byteArray0 = new byte[8];
        int int0 = queueInputStream0.read(byteArray0, (int) (byte) 0, (int) (byte) 0);
        assertEquals(0, int0);
    }
}
