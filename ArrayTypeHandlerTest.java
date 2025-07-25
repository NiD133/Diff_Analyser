package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Types;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

  @Mock
  private Array mockArray;

  /**
   * Test setting a non-null array parameter.
   */
  @Override
  @Test
  public void testSetNonNullArrayParameter() throws Exception {
    TYPE_HANDLER.setParameter(ps, 1, mockArray, null);
    verify(ps).setArray(1, mockArray);
  }

  /**
   * Test setting a string array parameter.
   */
  @Test
  void testSetStringArrayParameter() throws Exception {
    Connection mockConnection = mock(Connection.class);
    when(ps.getConnection()).thenReturn(mockConnection);

    Array mockArray = mock(Array.class);
    when(mockConnection.createArrayOf(anyString(), any(String[].class))).thenReturn(mockArray);

    TYPE_HANDLER.setParameter(ps, 1, new String[] { "Hello World" }, JdbcType.ARRAY);
    verify(ps).setArray(1, mockArray);
    verify(mockArray).free();
  }

  /**
   * Test setting a null array parameter.
   */
  @Test
  void testSetNullArrayParameter() throws Exception {
    TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);
    verify(ps).setNull(1, Types.ARRAY);
  }

  /**
   * Test setting a parameter with an unsupported type.
   */
  @Test
  void testSetUnsupportedParameterType() {
    assertThrows(TypeException.class, () -> TYPE_HANDLER.setParameter(ps, 1, "unsupported parameter type", null));
  }

  /**
   * Test retrieving an array result from a ResultSet by column name.
   */
  @Override
  @Test
  public void testGetArrayResultFromResultSetByName() throws Exception {
    when(rs.getArray("column")).thenReturn(mockArray);
    String[] expectedArray = { "a", "b" };
    when(mockArray.getArray()).thenReturn(expectedArray);

    assertEquals(expectedArray, TYPE_HANDLER.getResult(rs, "column"));
    verify(mockArray).free();
  }

  /**
   * Test retrieving a null array result from a ResultSet by column name.
   */
  @Override
  @Test
  public void testGetNullArrayResultFromResultSetByName() throws Exception {
    when(rs.getArray("column")).thenReturn(null);
    assertNull(TYPE_HANDLER.getResult(rs, "column"));
  }

  /**
   * Test retrieving an array result from a ResultSet by column index.
   */
  @Override
  @Test
  public void testGetArrayResultFromResultSetByIndex() throws Exception {
    when(rs.getArray(1)).thenReturn(mockArray);
    String[] expectedArray = { "a", "b" };
    when(mockArray.getArray()).thenReturn(expectedArray);

    assertEquals(expectedArray, TYPE_HANDLER.getResult(rs, 1));
    verify(mockArray).free();
  }

  /**
   * Test retrieving a null array result from a ResultSet by column index.
   */
  @Override
  @Test
  public void testGetNullArrayResultFromResultSetByIndex() throws Exception {
    when(rs.getArray(1)).thenReturn(null);
    assertNull(TYPE_HANDLER.getResult(rs, 1));
  }

  /**
   * Test retrieving an array result from a CallableStatement.
   */
  @Override
  @Test
  public void testGetArrayResultFromCallableStatement() throws Exception {
    when(cs.getArray(1)).thenReturn(mockArray);
    String[] expectedArray = { "a", "b" };
    when(mockArray.getArray()).thenReturn(expectedArray);

    assertEquals(expectedArray, TYPE_HANDLER.getResult(cs, 1));
    verify(mockArray).free();
  }

  /**
   * Test retrieving a null array result from a CallableStatement.
   */
  @Override
  @Test
  public void testGetNullArrayResultFromCallableStatement() throws Exception {
    when(cs.getArray(1)).thenReturn(null);
    assertNull(TYPE_HANDLER.getResult(cs, 1));
  }
}