package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Iterator;

/**
 * Tests for the {@link ZippingIterator} class, focusing on constructor validation.
 */
public class ZippingIteratorTest {

    /**
     * Tests that the three-argument constructor throws a NullPointerException
     * when any of its iterator arguments are null. This behavior is part of the
     * constructor's public contract.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithThreeArgumentsShouldThrowExceptionForNullIterator() {
        // The constructor is documented to throw a NullPointerException if any iterator is null.
        // We pass three nulls to confirm this behavior. The explicit casts are necessary
        // to resolve ambiguity between the varargs and the three-argument constructors.
        new ZippingIterator<Object>(
            (Iterator<? extends Object>) null,
            (Iterator<? extends Object>) null,
            (Iterator<? extends Object>) null
        );
    }
}