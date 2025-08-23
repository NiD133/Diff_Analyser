package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

// Renamed class for clarity and to follow standard naming conventions.
// The "Test6" suffix was redundant and uninformative.
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

  @Override
  @Test
  // Method name improved to clearly state the behavior being tested:
  // what happens when a NULL value is read from the database by its column index.
  void shouldReturnNullWhenReadingNullValueByIndex() throws Exception {
    // Arrange: Mock the ResultSet to simulate reading a NULL integer value.
    // The JDBC specification states that reading a SQL NULL via getInt() should return 0.
    // The wasNull() method is then used to differentiate between a genuine 0 and a SQL NULL.
    when(rs.getInt(1)).thenReturn(0);
    when(rs.wasNull()).thenReturn(true);

    // Act: Retrieve the result from the type handler.
    MyEnum result = TYPE_HANDLER.getResult(rs, 1);

    // Assert: The handler should correctly interpret the SQL NULL and return a Java null.
    assertNull(result);
  }
}