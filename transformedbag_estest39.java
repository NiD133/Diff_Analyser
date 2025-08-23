package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Contains tests for the TransformedBag class, focusing on exception handling.
 */
public class TransformedBagTest {

    /**
     * Tests that calling remove() with a null object on a TransformedBag
     * backed by a TreeBag throws a NullPointerException.
     * <p>
     * The TransformedBag should delegate the remove operation directly to the
     * backing bag. A TreeBag, which uses natural ordering by default, does not
     * support null elements and will throw a NullPointerException when an
     * operation like remove() is attempted with null.
     */
    @Test
    public void testRemoveWithNullOnUnderlyingTreeBagThrowsNPE() {
        // Arrange
        // The transformer is not used by the remove() method, so any transformer will do.
        final Transformer<Object, Object> transformer = ConstantTransformer.nullTransformer();
        
        // A standard TreeBag does not allow null elements.
        final TreeBag<Object> backingBag = new TreeBag<>();
        final TransformedBag<Object> transformedBag = TransformedBag.transformingBag(backingBag, transformer);

        // Act & Assert
        try {
            // The number of copies to remove is irrelevant; the null object causes the exception.
            transformedBag.remove(null, 1);
            fail("Expected a NullPointerException because the underlying TreeBag cannot handle null objects.");
        } catch (final NullPointerException e) {
            // This is the expected behavior. The test passes.
        }
    }
}