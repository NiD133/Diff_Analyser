package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class AbstractIterator_ESTestTest3 extends AbstractIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        ArrayDeque<Object> arrayDeque0 = new ArrayDeque<Object>();
        ConsumingQueueIterator<Object> consumingQueueIterator0 = new ConsumingQueueIterator<Object>(arrayDeque0);
        Object object0 = consumingQueueIterator0.endOfData();
        assertNull(object0);
    }
}
