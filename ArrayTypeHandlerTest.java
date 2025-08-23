package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("ArrayTypeHandler")
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<Object> handler = new ArrayTypeHandler();

  private static final String COLUMN = "column";
  private static final int INDEX = 1;
  private static final String[] SAMPLE_VALUES = { "a", "b" };

  @Mock
  Array sqlArray;

  // ---------------------------------------------------------------------------
  // setParameter
  // ---------------------------------------------------------------------------

  @Override
  @Test
  @DisplayName("setParameter: passes Array directly to PreparedStatement")
  public void shouldSetParameter() throws Exception {
    handler.setParameter(ps, INDEX, sqlArray, null);

    verify(ps).setArray(INDEX, sqlArray);
  }

  @Test
  @DisplayName("setParameter: creates SQL Array for String[] and frees it")
  void shouldSetStringArrayParameter() throws Exception {
    // given
    Connection connection = mock(Connection.class);
    when(ps.getConnection()).thenReturn(connection);

    Array createdArray = mock(Array.class);
    // Note: using anyString() to avoid coupling to internal JDBC type mapping
    when(connection.createArrayOf(anyString(), any(String[].class))).thenReturn(createdArray);

    // when
    handler.setParameter(ps, INDEX, new String[] { "Hello World" }, JdbcType.ARRAY);

    // then
    verify(ps).setArray(INDEX, createdArray);
    verify(createdArray).free();
  }

  @Test
  @DisplayName("setParameter: sets SQL NULL for null parameter")
  void shouldSetNullParameter() throws Exception {
    handler.setParameter(ps, INDEX, null, JdbcType.ARRAY);

    verify(ps).setNull(INDEX, Types.ARRAY);
  }

  @Test
  @DisplayName("setParameter: throws TypeException for non-array parameter")
  void shouldFailForNonArrayParameter() {
    assertThrows(TypeException.class, () ->
        handler.setParameter(ps, INDEX, "unsupported parameter type", null));
  }

  // ---------------------------------------------------------------------------
  // getResult (ResultSet by name)
  // ---------------------------------------------------------------------------

  @Override
  @Test
  @DisplayName("getResult (ResultSet by name): returns extracted array and frees SQL Array")
  public void shouldGetResultFromResultSetByName() throws Exception {
    when(rs.getArray(COLUMN)).thenReturn(sqlArray);
    when(sqlArray.getArray()).thenReturn(SAMPLE_VALUES);

    Object result = handler.getResult(rs, COLUMN);

    assertArrayEquals(SAMPLE_VALUES, (String[]) result);
    verify(sqlArray).free();
  }

  @Override
  @Test
  @DisplayName("getResult (ResultSet by name): returns null when SQL Array is null")
  public void shouldGetResultNullFromResultSetByName() throws Exception {
    when(rs.getArray(COLUMN)).thenReturn(null);

    assertNull(handler.getResult(rs, COLUMN));
  }

  // ---------------------------------------------------------------------------
  // getResult (ResultSet by position)
  // ---------------------------------------------------------------------------

  @Override
  @Test
  @DisplayName("getResult (ResultSet by position): returns extracted array and frees SQL Array")
  public void shouldGetResultFromResultSetByPosition() throws Exception {
    when(rs.getArray(INDEX)).thenReturn(sqlArray);
    when(sqlArray.getArray()).thenReturn(SAMPLE_VALUES);

    Object result = handler.getResult(rs, INDEX);

    assertArrayEquals(SAMPLE_VALUES, (String[]) result);
    verify(sqlArray).free();
  }

  @Override
  @Test
  @DisplayName("getResult (ResultSet by position): returns null when SQL Array is null")
  public void shouldGetResultNullFromResultSetByPosition() throws Exception {
    when(rs.getArray(INDEX)).thenReturn(null);

    assertNull(handler.getResult(rs, INDEX));
  }

  // ---------------------------------------------------------------------------
  // getResult (CallableStatement)
  // ---------------------------------------------------------------------------

  @Override
  @Test
  @DisplayName("getResult (CallableStatement): returns extracted array and frees SQL Array")
  public void shouldGetResultFromCallableStatement() throws Exception {
    when(cs.getArray(INDEX)).thenReturn(sqlArray);
    when(sqlArray.getArray()).thenReturn(SAMPLE_VALUES);

    Object result = handler.getResult(cs, INDEX);

    assertArrayEquals(SAMPLE_VALUES, (String[]) result);
    verify(sqlArray).free();
  }

  @Override
  @Test
  @DisplayName("getResult (CallableStatement): returns null when SQL Array is null")
  public void shouldGetResultNullFromCallableStatement() throws Exception {
    when(cs.getArray(INDEX)).thenReturn(null);

    assertNull(handler.getResult(cs, INDEX));
  }
}