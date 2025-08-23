package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ElementWithBrokenMethods elementWithBrokenMethods;

    /**
     * A helper class that simulates an object where calling hashCode() or equals()
     * is unsafe and results in an exception. This is a common scenario with
     * poorly implemented objects or certain mocks.
     */
    private static class ElementWithBrokenMethods {
        @Override
        public final int hashCode() {
            throw new NullPointerException("hashCode() is broken");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("equals() is broken");
        }
    }

    /**
     * Verifies that toString() works correctly even if the set contains an element
     * whose hashCode() or equals() methods throw an exception. A standard
     * java.util.HashSet would fail in this scenario, but HashCodeAndEqualsSafeSet
     * is designed to handle it gracefully.
     */
    @Test
    public void toString_should_not_fail_for_element_with_problematic_hashCode_and_equals() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(elementWithBrokenMethods);

        // Act
        String toStringResult = set.toString();

        // Assert
        // The key is that the call to toString() does not throw an exception.
        // We also assert that the result is a non-empty string, which is the
        // expected behavior for a non-empty collection.
        assertThat(toStringResult).isNotEmpty();
    }
}