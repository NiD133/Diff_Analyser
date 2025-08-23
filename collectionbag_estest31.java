package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.LinkedList;

/**
 * This class contains an improved version of an auto-generated test for {@link CollectionBag}.
 * The original class name and inheritance from a test scaffolding class are preserved for context,
 * but the test logic has been rewritten for human readability.
 */
public class CollectionBag_ESTestTest31 extends CollectionBag_ESTest_scaffolding {

    /**
     * Tests that a {@link ClassCastException} from a decorated bag is correctly propagated
     * by the {@link CollectionBag}.
     * <p>
     * This test sets up a scenario where a {@link CollectionBag} wraps a {@link TreeBag}.
     * A {@code TreeBag} requires its elements to be {@link Comparable} to maintain sort order.
     * The test verifies that attempting to add a non-comparable element (a {@link LinkedList})
     * results in the expected {@code ClassCastException} from the underlying {@code TreeBag}.
     */
    @Test(expected = ClassCastException.class)
    public void addShouldPropagateClassCastExceptionFromUnderlyingSortedBag() {
        // Arrange: Create a TreeBag, which requires comparable elements, and decorate it.
        // We use LinkedList as an example of a type that is not Comparable.
        final Bag<LinkedList<Object>> underlyingSortedBag = new TreeBag<>();
        final Bag<LinkedList<Object>> collectionBag = new CollectionBag<>(underlyingSortedBag);
        final LinkedList<Object> nonComparableElement = new LinkedList<>();

        // Act: Attempt to add the non-comparable element. The CollectionBag delegates
        // this call to the underlying TreeBag, which will throw the exception.
        collectionBag.add(nonComparableElement);

        // Assert: The test is expected to throw a ClassCastException, which is
        // verified by the @Test(expected) annotation.
    }
}