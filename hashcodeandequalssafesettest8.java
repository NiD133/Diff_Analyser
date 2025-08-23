package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

// The class name "HashCodeAndEqualsSafeSetTestTest8" is a bit redundant.
// A better name would be "HashCodeAndEqualsSafeSetTest".
public class HashCodeAndEqualsSafeSetTestTest8 {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ObjectWithFailingHashCodeAndEquals mockWithFailingMethods;

    @Mock
    private Observer regularMock;

    /**
     * Helper class simulating an object whose hashCode() or equals() methods are "broken"
     * and throw an exception upon invocation. This is used to test the "safe" nature
     * of the HashCodeAndEqualsSafeSet.
     */
    private static class ObjectWithFailingHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new UnsupportedOperationException("Failing hashCode() for test purposes");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new UnsupportedOperationException("Failing equals() for test purposes");
        }
    }

    @Test
    public void shouldIterateSuccessfullyWhenSetContainsObjectWithFailingHashCodeAndEquals() {
        // This test verifies that HashCodeAndEqualsSafeSet can be iterated over
        // without throwing an exception, even when it contains an object
        // whose hashCode() and equals() methods are designed to fail.

        // Arrange
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods, regularMock);

        // Act
        List<Object> iteratedElements = new ArrayList<>();
        // The for-each loop implicitly calls iterator(), which is the behavior under test.
        for (Object element : safeSet) {
            iteratedElements.add(element);
        }

        // Assert
        // Verify that the iteration was successful and collected all the original elements.
        // This is a stronger and clearer assertion than just checking if the list is not empty.
        assertThat(iteratedElements).containsExactlyInAnyOrder(mockWithFailingMethods, regularMock);
    }
}