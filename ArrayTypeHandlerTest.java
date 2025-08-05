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

  private static final TypeHandler<Object> ARRAY_TYPE_HANDLER = new ArrayTypeHandler();

  @Mock
  private Array mockArray;

  /**
   * Test setting a non-null array parameter in a PreparedStatement.
   */
  @Override
  @Test
  public void testSetNonNullArrayParameter() throws Exception {
    ARRAY_TYPE_HANDLER.setParameter(ps, 1, mockArray, null);
    verify(ps).setArray(1, mockArray);
  }

  /**
   * Test setting a String array parameter in a PreparedStatement.
   */
  @Test
  void testSetStringArrayParameter() throws Exception {
    Connection mockConnection = mock(Connection.class);
    when(ps.getConnection()).thenReturn(mockConnection);

    Array mockArray = mock(Array.class);
    when(mockConnection.createArrayOf(anyString(), any(String[].class))).thenReturn(mockArray);

    ARRAY_TYPE_HANDLER.setParameter(ps, 1, new String[] { "Hello World" }, JdbcType.ARRAY);
    verify(ps).setArray(1, mockArray);
    verify(mockArray).free();
  }

  /**
   * Test setting a null array parameter in a PreparedStatement.
   */
  @Test
  void testSetNullArrayParameter() throws Exception {
    ARRAY_TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);
    verify(ps).setNull(1, Types.ARRAY);
  }

  /**
   * Test setting a non-array parameter throws a TypeException.
   */
  @Test
  void testSetNonArrayParameterThrowsException() {
    assertThrows(TypeException.class, () -> 
      ARRAY_TYPE_HANDLER.setParameter(ps, 1, "unsupported parameter type", null)
    );
  }

  /**
   * Test getting a result from a ResultSet by column name.
   */
  @Override
  @Test
  public void testGetResultFromResultSetByName() throws Exception {
    when(rs.getArray("column")).thenReturn(mockArray);
    String[] expectedArray = { "a", "b" };
    when(mockArray.getArray()).thenReturn(expectedArray);

    assertEquals(expectedArray, ARRAY_TYPE_HANDLER.getResult(rs, "column"));
    verify(mockArray).free();
  }

  /**
   * Test getting a null result from a ResultSet by column name.
   */
  @Override
  @Test
  public void testGetNullResultFromResultSetByName() throws Exception {
    when(rs.getArray("column")).thenReturn(null);
    assertNull(ARRAY_TYPE_HANDLER.getResult(rs, "column"));
  }

  /**
   * Test getting a result from a ResultSet by column index.
   */
  @Override
  @Test
  public void testGetResultFromResultSetByPosition() throws Exception {
    when(rs.getArray(1)).thenReturn(mockArray);
    String[] expectedArray = { "a", "b" };
    when(mockArray.getArray()).thenReturn(expectedArray);

    assertEquals(expectedArray, ARRAY_TYPE_HANDLER.getResult(rs, 1));
    verify(mockArray).free();
  }

  /**
   * Test getting a null result from a ResultSet by column index.
   */
  @Override
  @Test
  public void testGetNullResultFromResultSetByPosition() throws Exception {
    when(rs.getArray(1)).thenReturn(null);
    assertNull(ARRAY_TYPE_HANDLER.getResult(rs, 1));
  }

  /**
   * Test getting a result from a CallableStatement by column index.
   */
  @Override
  @Test
  public void testGetResultFromCallableStatement() throws Exception {
    when(cs.getArray(1)).thenReturn(mockArray);
    String[] expectedArray = { "a", "b" };
    when(mockArray.getArray()).thenReturn(expectedArray);

    assertEquals(expectedArray, ARRAY_TYPE_HANDLER.getResult(cs, 1));
    verify(mockArray).free();
  }

  /**
   * Test getting a null result from a CallableStatement by column index.
   */
  @Override
  @Test
  public void testGetNullResultFromCallableStatement() throws Exception {
    when(cs.getArray(1)).thenReturn(null);
    assertNull(ARRAY_TYPE_HANDLER.getResult(cs, 1));
  }
}