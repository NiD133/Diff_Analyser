package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

/**
 * Tests for the EnumOrdinalTypeHandler.
 *
 * This test suite verifies that the EnumOrdinalTypeHandler correctly handles the conversion
 * between a Java Enum and its corresponding ordinal value (integer) for database operations.
 *
 * It overrides tests from BaseTypeHandlerTest to provide specific mocks for an
 * integer-based ordinal handler.
 */
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  // A self-contained enum for testing purposes, making the test easier to understand.
  private enum MyEnum {
    ONE, // ordinal = 0
    TWO  // ordinal = 1
  }

  private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

  @Override
  @Test
  public void shouldSetParameter() throws Exception {
    // Arrange: Define the enum parameter to be set.
    MyEnum parameter = MyEnum.TWO;
    int expectedOrdinal = 1; // The ordinal of MyEnum.TWO

    // Act: Ask the handler to set the parameter on the PreparedStatement.
    TYPE_HANDLER.setParameter(ps, 1, parameter, null);

    // Assert: Verify the handler called setInt() with the correct ordinal value.
    verify(ps).setInt(1, expectedOrdinal);
  }

  @Override
  @Test
  public void shouldGetResultFromResultSetByName() throws Exception {
    // Arrange: Mock the ResultSet to return an enum's ordinal by column name.
    when(rs.getInt("column")).thenReturn(0); // The ordinal of MyEnum.ONE
    when(rs.wasNull()).thenReturn(false);

    // Act & Assert: Verify the handler correctly converts the ordinal to the enum.
    assertEquals(MyEnum.ONE, TYPE_HANDLER.getResult(rs, "column"));
  }

  @Override
  @Test
  public void shouldGetResultFromResultSetByIndex() throws Exception {
    // Arrange: Mock the ResultSet to return an enum's ordinal by column index.
    when(rs.getInt(1)).thenReturn(0); // The ordinal of MyEnum.ONE
    when(rs.wasNull()).thenReturn(false);

    // Act & Assert: Verify the handler correctly converts the ordinal to the enum.
    assertEquals(MyEnum.ONE, TYPE_HANDLER.getResult(rs, 1));
  }

  @Override
  @Test
  public void shouldGetResultFromCallableStatement() throws Exception {
    // Arrange: Mock the CallableStatement to return an enum's ordinal by column index.
    when(cs.getInt(1)).thenReturn(0); // The ordinal of MyEnum.ONE
    when(cs.wasNull()).thenReturn(false);

    // Act & Assert: Verify the handler correctly converts the ordinal to the enum.
    assertEquals(MyEnum.ONE, TYPE_HANDLER.getResult(cs, 1));
  }

  @Override
  @Test
  public void shouldGetResultFromResultSetByName_whenNull() throws Exception {
    // Arrange: Mock the ResultSet to return a SQL NULL value.
    when(rs.getInt("column")).thenReturn(0); // Value is irrelevant when wasNull is true
    when(rs.wasNull()).thenReturn(true);

    // Act & Assert: Verify the handler returns null for a SQL NULL.
    assertNull(TYPE_HANDLER.getResult(rs, "column"));
  }

  @Override
  @Test
  public void shouldGetResultFromResultSetByIndex_whenNull() throws Exception {
    // Arrange: Mock the ResultSet to return a SQL NULL value.
    when(rs.getInt(1)).thenReturn(0); // Value is irrelevant when wasNull is true
    when(rs.wasNull()).thenReturn(true);

    // Act & Assert: Verify the handler returns null for a SQL NULL.
    assertNull(TYPE_HANDLER.getResult(rs, 1));
  }

  @Override
  @Test
  public void shouldGetResultFromCallableStatement_whenNull() throws Exception {
    // Arrange: Mock the CallableStatement to return a SQL NULL value.
    when(cs.getInt(1)).thenReturn(0); // Value is irrelevant when wasNull is true
    when(cs.wasNull()).thenReturn(true);

    // Act & Assert: Verify the handler returns null for a SQL NULL.
    assertNull(TYPE_HANDLER.getResult(cs, 1));
  }
}