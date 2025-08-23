package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link EnumOrdinalTypeHandler} to verify its handling of null values.
 */
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  // Define the enum used in the test for clarity and completeness.
  private enum MyEnum {
    ONE, TWO
  }

  private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

  @Override
  @Test
  @DisplayName("Should return null when reading from a CallableStatement that reports a SQL NULL")
  void shouldGetResultNullFromCallableStatement() throws Exception {
    // Arrange: Mock the CallableStatement to simulate a SQL NULL value.
    // According to the JDBC specification, if the value read by getInt() was SQL NULL,
    // the value returned is 0. The wasNull() method must be called immediately after
    // to determine if the last value read was actually NULL.
    when(cs.getInt(1)).thenReturn(0);
    when(cs.wasNull()).thenReturn(true);

    // Act: Retrieve the result using the type handler.
    MyEnum result = TYPE_HANDLER.getResult(cs, 1);

    // Assert: The handler should correctly interpret the SQL NULL and return a Java null.
    assertNull(result, "The type handler should return null for a SQL NULL value.");
  }
}