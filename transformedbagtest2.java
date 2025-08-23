package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

public class TransformedBagTestTest2<T> extends AbstractBagTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        // This factory method creates a bag that transforms elements as they are added.
        // The NOOP_TRANSFORMER doesn't change the elements, which is suitable for the
        // generic tests in the abstract parent class.
        return TransformedBag.transformingBag(new HashBag<>(), (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    /**
     * Tests that the {@link TransformedBag#transformedBag(Bag, Transformer)} factory method
     * correctly transforms all existing elements of the decorated bag upon creation.
     */
    @Test
    @SuppressWarnings("unchecked") // Required for casting the raw Transformer from TransformedCollectionTest
    void testTransformedBagFactory_transformsExistingElements() {
        // Arrange: Create a source bag with string elements.
        final Bag<String> sourceBag = new HashBag<>();
        final String[] stringElements = {"1", "3", "5", "7", "2", "4", "6"};
        for (final String element : stringElements) {
            sourceBag.add(element);
        }

        final Transformer<String, Integer> stringToInteger =
            (Transformer<String, Integer>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // Act: Decorate the source bag. This factory method is expected to convert
        // all existing elements from String to Integer.
        final Bag<Integer> transformedBag = TransformedBag.transformedBag(sourceBag, stringToInteger);

        // Assert
        assertEquals(stringElements.length, transformedBag.size(), "Size should be maintained after transformation");

        // Verify the bag contains the transformed integers, not the original strings.
        for (final String originalElement : stringElements) {
            final Integer transformedElement = Integer.valueOf(originalElement);
            assertTrue(transformedBag.contains(transformedElement),
                "Bag should contain the transformed element: " + transformedElement);
            assertFalse(transformedBag.contains(originalElement),
                "Bag should not contain the original string element: " + originalElement);
        }

        // Verify remove behavior with a sample element.
        final String elementToRemoveAsString = stringElements[0]; // "1"
        final Integer elementToRemoveAsInteger = Integer.valueOf(elementToRemoveAsString);

        // Attempting to remove the original string should fail.
        assertFalse(transformedBag.remove(elementToRemoveAsString),
            "Should not be able to remove using the pre-transformation object type");

        // Removing the transformed integer should succeed.
        assertTrue(transformedBag.remove(elementToRemoveAsInteger),
            "Should be able to remove using the post-transformation object type");
    }
}