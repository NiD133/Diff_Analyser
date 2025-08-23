package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void cloneShouldThrowCloneNotSupportedException() {
        // Arrange: Create an instance of the set.
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();

        // Act & Assert: Verify that calling clone() throws CloneNotSupportedException,
        // as the class does not implement the Cloneable interface.
        assertThatThrownBy(set::clone)
            .isInstanceOf(CloneNotSupportedException.class)
            .withMessage(null); // The default implementation has no message
    }
}