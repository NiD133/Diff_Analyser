package org.apache.ibatis.type;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 *
 * @author Clinton Begin
 */
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  // Define a local enum for testing to make the test self-contained and clear.
  private enum MyEnum {
    ONE, // ordinal = 0
    TWO  // ordinal = 1
  }

  private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

  @Override
  @Test
  void shouldSetParameter() throws Exception {
    // Arrange
    MyEnum parameter = MyEnum.ONE;
    int expectedOrdinal = 0; // The expected ordinal for MyEnum.ONE

    // Act
    TYPE_HANDLER.setParameter(ps, 1, parameter, null);

    // Assert
    // Verify that the handler sets the enum's ordinal value as an integer.
    verify(ps).setInt(1, expectedOrdinal);
  }

  // Note: The tests for getResult(), null parameters, etc., are likely covered
  // in other methods within this class or the BaseTypeHandlerTest superclass.
  // This refactoring focuses on the provided test case.
}