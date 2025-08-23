package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.bag.AbstractMapBag; // Only needed for the original test's type
import org.apache.commons.collections4.bag.TreeBag;
import org.junit.Test;

import java.util.Collection;

/**
 * This class contains tests for the {@link TreeBag} class.
 * This specific test focuses on constructor behavior with invalid arguments.
 */
// The original class name is kept for context, but a name like TreeBagConstructorTest would be more descriptive.
public class TreeBag_ESTestTest16 extends TreeBag_ESTest_scaffolding {

    /**
     * Tests that the TreeBag constructor throws a NullPointerException
     * when initialized with a null collection.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullCollectionShouldThrowNullPointerException() {
        // Attempt to create a TreeBag from a null collection, which is not allowed.
        new TreeBag<String>((Collection<String>) null);
    }
}