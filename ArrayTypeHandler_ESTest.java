package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ArrayTypeHandlerTest {

  // ----------------------------
  // extractArray(Array) behavior
  // ----------------------------

  @Test
  public void extractArray_returnsNull_whenSqlArrayIsNull() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();

    Object result = handler.extractArray(null);

    assertNull(result);
  }

  @Test
  public void extractArray_returnsContents_andFreesSqlArray() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    Array sqlArray = mock(Array.class);
    String[] expected = new String[] {"a", "b"};
    when(sqlArray.getArray()).thenReturn(expected);

    Object actual = handler.extractArray(sqlArray);

    assertSame(expected, actual);
    verify(sqlArray).free(); // ensure resources are released
  }

  // -----------------------------------------------
  // setNonNullParameter(PreparedStatement, ...) path
  // -----------------------------------------------

  @Test
  public void setNonNullParameter_usesSqlArrayDirectly() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);
    Array sqlArray = mock(Array.class);

    handler.setNonNullParameter(ps, 1, sqlArray, JdbcType.ARRAY);

    verify(ps).setArray(1, sqlArray);
    verifyNoMoreInteractions(ps);
  }

  @Test
  public void setNonNullParameter_createsSqlArray_fromJavaArray() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);
    Connection conn = mock(Connection.class);
    when(ps.getConnection()).thenReturn(conn);

    String[] param = new String[] {"x", "y"};
    Array sqlArray = mock(Array.class);
    when(conn.createArrayOf("VARCHAR", param)).thenReturn(sqlArray);

    handler.setNonNullParameter(ps, 2, param, JdbcType.ARRAY);

    verify(conn).createArrayOf("VARCHAR", param);
    verify(ps).setArray(2, sqlArray);
  }

  @Test
  public void setNonNullParameter_rejectsUnsupportedParameterType() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);
    java.sql.Time unsupported = new java.sql.Time(0);

    RuntimeException ex = assertThrows(
        RuntimeException.class,
        () -> handler.setNonNullParameter(ps, 3, unsupported, JdbcType.BINARY)
    );

    assertTrue(ex.getMessage().contains("requires SQL array or java array parameter"));
    assertTrue(ex.getMessage().contains("java.sql.Time"));
  }

  // --------------------------------
  // resolveTypeName(Class<?>) mapping
  // --------------------------------

  @Test
  public void resolveTypeName_mapsJavaUtilDate_toTimestamp() {
    ArrayTypeHandler handler = new ArrayTypeHandler();

    String typeName = handler.resolveTypeName(java.util.Date.class);

    assertEquals("TIMESTAMP", typeName);
  }

  @Test
  public void resolveTypeName_nullType_throwsNPE() {
    ArrayTypeHandler handler = new ArrayTypeHandler();

    assertThrows(NullPointerException.class, () -> handler.resolveTypeName(null));
  }

  // ------------------------------------------
  // getNullableResult(...) extraction behavior
  // ------------------------------------------

  @Test
  public void getNullableResult_fromCallableStatement_returnsNull_whenSqlArrayIsNull() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getArray(1)).thenReturn(null);

    Object result = handler.getNullableResult(cs, 1);

    assertNull(result);
  }

  @Test
  public void getNullableResult_fromCallableStatement_returnsContents_andFreesArray() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    Array sqlArray = mock(Array.class);
    Integer[] expected = new Integer[] {1, 2, 3};
    when(sqlArray.getArray()).thenReturn(expected);
    when(cs.getArray(7)).thenReturn(sqlArray);

    Object actual = handler.getNullableResult(cs, 7);

    assertSame(expected, actual);
    verify(sqlArray).free();
  }

  @Test
  public void getNullableResult_fromResultSetByName_returnsContents_andFreesArray() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    ResultSet rs = mock(ResultSet.class);
    Array sqlArray = mock(Array.class);
    Long[] expected = new Long[] {10L, 20L};
    when(sqlArray.getArray()).thenReturn(expected);
    when(rs.getArray("col")).thenReturn(sqlArray);

    Object actual = handler.getNullableResult(rs, "col");

    assertSame(expected, actual);
    verify(sqlArray).free();
  }

  @Test
  public void getNullableResult_fromResultSetByIndex_returnsNull_whenSqlArrayIsNull() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    ResultSet rs = mock(ResultSet.class);
    when(rs.getArray(5)).thenReturn(null);

    Object result = handler.getNullableResult(rs, 5);

    assertNull(result);
  }
}