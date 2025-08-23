package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.LinkedListMultimap;
import java.nio.CharBuffer;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class ForwardingQueue_ESTest extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testStandardOfferAndPoll() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(98);
        Object testObject = new Object();
        evictingQueue.standardOffer(testObject);
        Object polledObject = evictingQueue.standardPoll();
        assertSame(polledObject, testObject);
    }

    @Test(timeout = 4000)
    public void testAddAndStandardPeek() {
        EvictingQueue<Locale.FilteringMode> evictingQueue = EvictingQueue.create(1);
        Locale.FilteringMode filteringMode = Locale.FilteringMode.EXTENDED_FILTERING;
        evictingQueue.add(filteringMode);
        Locale.FilteringMode peekedMode = evictingQueue.standardPeek();
        assertSame(peekedMode, filteringMode);
    }

    @Test(timeout = 4000)
    public void testStandardOfferAndRemove() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(98);
        Object testObject = new Object();
        evictingQueue.standardOffer(testObject);
        Object removedObject = evictingQueue.remove();
        assertFalse(evictingQueue.contains(removedObject));
    }

    @Test(timeout = 4000)
    public void testAddAndPoll() {
        EvictingQueue<Locale.FilteringMode> evictingQueue = EvictingQueue.create(126);
        Locale.FilteringMode filteringMode = Locale.FilteringMode.REJECT_EXTENDED_RANGES;
        evictingQueue.add(filteringMode);
        Locale.FilteringMode polledMode = evictingQueue.poll();
        assertFalse(evictingQueue.contains(polledMode));
    }

    @Test(timeout = 4000)
    public void testAddAndPeek() {
        EvictingQueue<Locale.Category> evictingQueue = EvictingQueue.create(5760);
        Locale.Category category = Locale.Category.DISPLAY;
        evictingQueue.add(category);
        Locale.Category peekedCategory = evictingQueue.peek();
        assertTrue(evictingQueue.contains(peekedCategory));
    }

    @Test(timeout = 4000)
    public void testOfferWithLinkedListMultimap() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(613);
        LinkedListMultimap<Object, Object> multimap = LinkedListMultimap.create();
        boolean offerResult = evictingQueue.offer(multimap);
        assertTrue(offerResult);
    }

    @Test(timeout = 4000)
    public void testStandardOfferAndElement() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(1937);
        Object testObject = new Object();
        evictingQueue.standardOffer(testObject);
        Object element = evictingQueue.element();
        assertTrue(evictingQueue.contains(element));
    }

    @Test(timeout = 4000)
    public void testStandardOfferNullThrowsException() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(2014);
        try {
            evictingQueue.standardOffer(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testOfferNullThrowsException() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(2);
        try {
            evictingQueue.offer(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testStandardPollOnEmptyQueue() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(98);
        Object polledObject = evictingQueue.standardPoll();
        assertNull(polledObject);
    }

    @Test(timeout = 4000)
    public void testStandardPeekOnEmptyQueue() {
        EvictingQueue<Comparable<CharBuffer>> evictingQueue = EvictingQueue.create(0);
        Comparable<CharBuffer> peekedObject = evictingQueue.standardPeek();
        assertNull(peekedObject);
    }

    @Test(timeout = 4000)
    public void testRemoveOnEmptyQueueThrowsException() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(98);
        try {
            evictingQueue.remove();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("java.util.ArrayDeque", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekOnEmptyQueue() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(0);
        Object peekedObject = evictingQueue.peek();
        assertNull(peekedObject);
    }

    @Test(timeout = 4000)
    public void testElementOnEmptyQueueThrowsException() {
        EvictingQueue<Object> evictingQueue = EvictingQueue.create(1937);
        try {
            evictingQueue.element();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("java.util.ArrayDeque", e);
        }
    }

    @Test(timeout = 4000)
    public void testPollOnEmptyQueue() {
        EvictingQueue<Locale.FilteringMode> evictingQueue = EvictingQueue.create(126);
        evictingQueue.poll();
    }
}