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

public class AbstractIterator_ESTestTest2 extends AbstractIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        PriorityQueue<Locale.FilteringMode> priorityQueue0 = new PriorityQueue<Locale.FilteringMode>();
        Locale.FilteringMode locale_FilteringMode0 = Locale.FilteringMode.REJECT_EXTENDED_RANGES;
        priorityQueue0.add(locale_FilteringMode0);
        ConsumingQueueIterator<Locale.FilteringMode> consumingQueueIterator0 = new ConsumingQueueIterator<Locale.FilteringMode>(priorityQueue0);
        boolean boolean0 = consumingQueueIterator0.hasNext();
        assertTrue(boolean0);
    }
}
