package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import java.util.Iterator;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling next() on an ObjectGraphIterator throws a NullPointerException
     * when its underlying InvokerTransformer is initialized with a null method name.
     * The iterator attempts to use the transformer to find the next element, which fails.
     */
    @Test(expected = NullPointerException.class)
    public void testNextThrowsNpeWhenUsingInvokerTransformerWithNullMethodName() {
        // Arrange: Create an iterator with a root element and a transformer
        // that is guaranteed to fail.
        final Integer rootElement = 42;
        final Transformer<Integer, Integer> faultyTransformer = new InvokerTransformer<>(null);
        final Iterator<Integer> iterator = new ObjectGraphIterator<>(rootElement, faultyTransformer);

        // Act & Assert: Calling next() should trigger the transformer, which will
        // throw a NullPointerException because its method name is null.
        // The @Test(expected=...) annotation handles the assertion.
        iterator.next();
    }
}