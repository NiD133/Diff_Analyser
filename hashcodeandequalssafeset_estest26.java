package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void shouldHaveSizeZeroWhenCreated() {
        // Arrange
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();

        // Act
        int size = emptySet.size();

        // Assert
        assertEquals("A newly created set should be empty", 0, size);
    }
}