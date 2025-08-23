package org.apache.ibatis.type;

import org.junit.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 */
public class ArrayTypeHandlerTest {

    /**
     * Verifies that resolveTypeName throws a NullPointerException when the input class is null.
     * This is the expected behavior as the method attempts to use the input as a key
     * in an internal map, which does not permit null keys.
     */
    @Test(expected = NullPointerException.class)
    public void resolveTypeNameShouldThrowNullPointerExceptionForNullInput() {
        // Arrange
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

        // Act
        // The following call is expected to throw a NullPointerException,
        // which is verified by the @Test(expected=...) annotation.
        arrayTypeHandler.resolveTypeName(null);
    }
}