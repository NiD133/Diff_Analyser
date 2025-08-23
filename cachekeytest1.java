package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the equality contract of the {@link CacheKey} class.
 */
@DisplayName("CacheKey Equality")
class CacheKeyTest {

    @Test
    @DisplayName("Two keys with identical components should be equal")
    void twoKeysWithIdenticalComponentsShouldBeEqual() {
        // Arrange: Create two sets of components that are identical in value.
        // A new Date object is created for each key to ensure the test checks for
        // value equality (.equals()) rather than reference equality (==).
        Date sharedDate = new Date();
        Object[] firstKeyComponents = { 1, "hello", null, new Date(sharedDate.getTime()) };
        Object[] secondKeyComponents = { 1, "hello", null, new Date(sharedDate.getTime()) };

        CacheKey firstKey = new CacheKey(firstKeyComponents);
        CacheKey secondKey = new CacheKey(secondKeyComponents);

        // Act & Assert: Verify that the two keys are equal according to the equals/hashCode contract.
        // assertAll ensures all assertions are checked, providing more comprehensive feedback on failure.
        assertAll("A CacheKey should be equal to another with the same components",
            () -> assertEquals(firstKey, secondKey, "Keys should be equal."),
            () -> assertEquals(secondKey, firstKey, "Equality should be symmetric."),
            () -> assertEquals(firstKey.hashCode(), secondKey.hashCode(), "Hash codes must be equal for equal objects."),
            () -> assertEquals(firstKey.toString(), secondKey.toString(), "String representations should be identical.")
        );
    }
}