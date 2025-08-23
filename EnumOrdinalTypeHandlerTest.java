package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  // Define an enum for testing purposes
  enum MyEnum {
    ONE, TWO
  }

  // TypeHandler instance for MyEnum
  private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

  @BeforeEach
  void setUp() {
    // Reset mock objects before each test if necessary
  }

  @Test
  void testSetParameterWithEnumValue() throws Exception {
    // Arrange
    int parameterIndex = 1;
    MyEnum enumValue = MyEnum.ONE;

    // Act
    TYPE_HANDLER.setParameter(ps, parameterIndex, enumValue, null);

    // Assert
    verify(ps).setInt(parameterIndex, enumValue.ordinal());
  }

  @Test
  void testSetParameterWithNullValue() throws Exception {
    // Arrange
    int parameterIndex = 1;
    JdbcType jdbcType = JdbcType.VARCHAR;

    // Act
    TYPE_HANDLER.setParameter(ps, parameterIndex, null, jdbcType);

    // Assert
    verify(ps).setNull(parameterIndex, jdbcType.TYPE_CODE);
  }

  @Test
  void testGetResultFromResultSetByName() throws Exception {
    // Arrange
    String columnName = "column";
    when(rs.getInt(columnName)).thenReturn(0);
    when(rs.wasNull()).thenReturn(false);

    // Act
    MyEnum result = TYPE_HANDLER.getResult(rs, columnName);

    // Assert
    assertEquals(MyEnum.ONE, result);
  }

  @Test
  void testGetResultNullFromResultSetByName() throws Exception {
    // Arrange
    String columnName = "column";
    when(rs.getInt(columnName)).thenReturn(0);
    when(rs.wasNull()).thenReturn(true);

    // Act
    MyEnum result = TYPE_HANDLER.getResult(rs, columnName);

    // Assert
    assertNull(result);
  }

  @Test
  void testGetResultFromResultSetByPosition() throws Exception {
    // Arrange
    int columnIndex = 1;
    when(rs.getInt(columnIndex)).thenReturn(0);
    when(rs.wasNull()).thenReturn(false);

    // Act
    MyEnum result = TYPE_HANDLER.getResult(rs, columnIndex);

    // Assert
    assertEquals(MyEnum.ONE, result);
  }

  @Test
  void testGetResultNullFromResultSetByPosition() throws Exception {
    // Arrange
    int columnIndex = 1;
    when(rs.getInt(columnIndex)).thenReturn(0);
    when(rs.wasNull()).thenReturn(true);

    // Act
    MyEnum result = TYPE_HANDLER.getResult(rs, columnIndex);

    // Assert
    assertNull(result);
  }

  @Test
  void testGetResultFromCallableStatement() throws Exception {
    // Arrange
    int columnIndex = 1;
    when(cs.getInt(columnIndex)).thenReturn(0);
    when(cs.wasNull()).thenReturn(false);

    // Act
    MyEnum result = TYPE_HANDLER.getResult(cs, columnIndex);

    // Assert
    assertEquals(MyEnum.ONE, result);
  }

  @Test
  void testGetResultNullFromCallableStatement() throws Exception {
    // Arrange
    int columnIndex = 1;
    when(cs.getInt(columnIndex)).thenReturn(0);
    when(cs.wasNull()).thenReturn(true);

    // Act
    MyEnum result = TYPE_HANDLER.getResult(cs, columnIndex);

    // Assert
    assertNull(result);
  }
}