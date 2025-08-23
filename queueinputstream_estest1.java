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

public class QueueInputStream_ESTestTest1 extends QueueInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        QueueInputStream.Builder queueInputStream_Builder0 = QueueInputStream.builder();
        ChronoUnit chronoUnit0 = ChronoUnit.HOURS;
        Duration duration0 = chronoUnit0.getDuration();
        QueueInputStream.Builder queueInputStream_Builder1 = queueInputStream_Builder0.setTimeout(duration0);
        assertSame(queueInputStream_Builder0, queueInputStream_Builder1);
    }
}
