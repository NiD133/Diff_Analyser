package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 */
class EnumOrdinalTypeHandlerTest {

  /**
   * The constructor should reject null arguments to ensure type safety.
   * This test verifies that an {@link IllegalArgumentException} is thrown
   * when the handler is instantiated with a null enum type.
   */
  @Test
  void constructorShouldThrowIllegalArgumentExceptionForNullType() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      // Attempt to create a handler with a null type class
      new EnumOrdinalTypeHandler<>(null);
    });

    // Verify that the exception message is correct
    assertEquals("Type argument cannot be null", exception.getMessage());
  }
}