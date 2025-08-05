package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.google.common.collect.ConsumingQueueIterator;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class AbstractIterator_ESTest extends AbstractIterator_ESTest_scaffolding {

    private static final Locale.FilteringMode TEST_FILTERING_MODE = Locale.FilteringMode.REJECT_EXTENDED_RANGES;

    @Test(timeout = 4000)
    public void testNextReturnsCorrectElement() throws Throwable {
        PriorityQueue<Locale.FilteringMode> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(TEST_FILTERING_MODE);
        ConsumingQueueIterator<Locale.FilteringMode> iterator = new ConsumingQueueIterator<>(priorityQueue);

        Locale.FilteringMode result = iterator.next();

        assertSame("The next element should be the one added to the queue", TEST_FILTERING_MODE, result);
    }

    @Test(timeout = 4000)
    public void testHasNextReturnsTrueWhenElementExists() throws Throwable {
        PriorityQueue<Locale.FilteringMode> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(TEST_FILTERING_MODE);
        ConsumingQueueIterator<Locale.FilteringMode> iterator = new ConsumingQueueIterator<>(priorityQueue);

        boolean hasNext = iterator.hasNext();

        assertTrue("hasNext should return true when there is an element", hasNext);
    }

    @Test(timeout = 4000)
    public void testEndOfDataReturnsNull() throws Throwable {
        ArrayDeque<Object> emptyQueue = new ArrayDeque<>();
        ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(emptyQueue);

        Object result = iterator.endOfData();

        assertNull("endOfData should return null", result);
    }

    @Test(timeout = 4000)
    public void testForEachRemainingOnEmptyQueue() throws Throwable {
        ArrayDeque<Object> emptyQueue = new ArrayDeque<>();
        ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(emptyQueue);
        Consumer<Object> mockConsumer = mock(Consumer.class, new ViolatedAssumptionAnswer());

        iterator.forEachRemaining(mockConsumer);

        boolean hasNext = iterator.hasNext();

        assertFalse("hasNext should return false after forEachRemaining on an empty queue", hasNext);
    }

    @Test(timeout = 4000)
    public void testPeekAndForEachRemaining() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        Object testObject = new Object();
        list.add(testObject);
        ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(list);

        iterator.peek();
        Consumer<Object> mockConsumer = mock(Consumer.class, new ViolatedAssumptionAnswer());
        iterator.forEachRemaining(mockConsumer);
    }

    @Test(timeout = 4000)
    public void testNextThrowsExceptionWhenNoElement() throws Throwable {
        PriorityQueue<Locale.FilteringMode> emptyQueue = new PriorityQueue<>();
        ConsumingQueueIterator<Locale.FilteringMode> iterator = new ConsumingQueueIterator<>(emptyQueue);

        try {
            iterator.next();
            fail("Expecting NoSuchElementException when calling next on an empty queue");
        } catch (NoSuchElementException e) {
            verifyException("com.google.common.collect.AbstractIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekThrowsExceptionWhenNoElement() throws Throwable {
        LinkedList<Object> emptyList = new LinkedList<>();
        ConsumingQueueIterator<Object> iterator = new ConsumingQueueIterator<>(emptyList);
        Consumer<Object> mockConsumer = mock(Consumer.class, new ViolatedAssumptionAnswer());

        iterator.forEachRemaining(mockConsumer);

        try {
            iterator.peek();
            fail("Expecting NoSuchElementException when calling peek on an empty iterator");
        } catch (NoSuchElementException e) {
            verifyException("com.google.common.collect.AbstractIterator", e);
        }
    }
}