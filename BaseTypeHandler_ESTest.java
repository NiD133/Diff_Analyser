package org.apache.ibatis.type;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.YearMonth;

import org.junit.Test;

/**
 * Readable tests for several BaseTypeHandler implementations.
 * 
 * Goals:
 * - Use meaningful test names that describe behavior.
 * - Avoid EvoSuite-specific runners and scaffolding.
 * - Verify realistic behavior instead of provoking arbitrary NPEs.
 * - Assert error messages only when MyBatis is expected to wrap errors.
 */
public class BaseTypeHandlerTest {

  // ---------- StringTypeHandler ----------

  @Test
  public void stringTypeHandler_setsStringParameter_whenValueIsNonNull() throws Exception {
    StringTypeHandler handler = new StringTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    handler.setParameter(ps, 1, "hello", JdbcType.VARCHAR);

    verify(ps).setString(1, "hello");
  }

  @Test
  public void stringTypeHandler_readsStringFromCallableStatement() throws Exception {
    StringTypeHandler handler = new StringTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getString(1)).thenReturn("value-from-db");

    String actual = handler.getResult(cs, 1);

    assertEquals("value-from-db", actual);
  }

  // ---------- YearMonthTypeHandler ----------

  @Test
  public void yearMonthTypeHandler_returnsNull_whenCallableStatementReturnsNull() throws Exception {
    YearMonthTypeHandler handler = new YearMonthTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getString(anyInt())).thenReturn(null);

    YearMonth actual = handler.getResult(cs, 1);

    assertNull(actual);
  }

  @Test
  public void yearMonthTypeHandler_parsesIsoYearMonthString() throws Exception {
    YearMonthTypeHandler handler = new YearMonthTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getString(1)).thenReturn("2021-05");

    YearMonth actual = handler.getResult(cs, 1);

    assertEquals(YearMonth.of(2021, 5), actual);
  }

  // ---------- ObjectTypeHandler ----------

  @Test
  public void objectTypeHandler_returnsNull_whenCallableStatementObjectIsNull() throws Exception {
    ObjectTypeHandler handler = new ObjectTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getObject(1)).thenReturn(null);

    Object actual = handler.getNullableResult(cs, 1);

    assertNull(actual);
  }

  // ---------- EnumOrdinalTypeHandler ----------

  @Test
  public void enumOrdinalTypeHandler_readsEnumByOrdinal_and_canBeUsedForSettingParameter() throws Exception {
    EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getInt(1)).thenReturn(0);
    when(cs.wasNull()).thenReturn(false);

    JdbcType read = handler.getNullableResult(cs, 1);
    assertEquals(JdbcType.values()[0], read);

    PreparedStatement ps = mock(PreparedStatement.class);
    // Should not throw; typically sets the ordinal back to the statement.
    handler.setNonNullParameter(ps, 1, read, JdbcType.INTEGER);
    // In most implementations, ordinal is set as an int parameter.
    verify(ps).setInt(1, read.ordinal());
  }

  @Test
  public void enumOrdinalTypeHandler_throwsHelpfulError_whenOrdinalIsOutOfRange() throws Exception {
    EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getInt(1)).thenReturn(-983);
    when(cs.wasNull()).thenReturn(false);

    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> handler.getNullableResult(cs, 1));

    assertTrue(ex.getMessage().contains("Cannot convert -983 to JdbcType"));
  }

  // ---------- ArrayTypeHandler ----------

  @Test
  public void arrayTypeHandler_rejectsNonArrayParameters() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    RuntimeException ex = assertThrows(
        RuntimeException.class,
        // bogus parameter type on purpose
        () -> handler.setNonNullParameter(ps, 1, new MonthTypeHandler(), JdbcType.ARRAY));

    assertTrue(ex.getMessage().contains("requires SQL array or java array parameter"));
  }

  // ---------- BaseTypeHandler null-handling and error wrapping ----------

  @Test
  public void setParameter_requiresJdbcType_whenParameterIsNull() throws Exception {
    ClobTypeHandler handler = new ClobTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    RuntimeException ex = assertThrows(
        RuntimeException.class,
        () -> handler.setParameter(ps, 1, null, null));

    assertTrue(ex.getMessage().contains("JdbcType must be specified for all nullable parameters"));
  }

  @Test
  public void setParameter_wrapsErrorsWithDescriptiveMessage_whenPsFails_andParamIsNonNull() {
    ClobTypeHandler handler = new ClobTypeHandler();
    // Intentionally pass a null PreparedStatement to simulate an infrastructure failure.
    RuntimeException ex = assertThrows(
        RuntimeException.class,
        () -> handler.setParameter(null, 5, "abc", JdbcType.CLOB));

    assertTrue(ex.getMessage().contains("Error setting non null for parameter #5"));
    assertTrue(ex.getMessage().contains("JdbcType CLOB"));
  }

  @Test
  public void getResult_fromCallableStatement_wrapsErrorsWithColumnIndex() {
    ClobTypeHandler handler = new ClobTypeHandler();

    RuntimeException ex = assertThrows(
        RuntimeException.class,
        () -> handler.getResult((CallableStatement) null, 7));

    assertTrue(ex.getMessage().contains("Error attempting to get column #7 from callable statement"));
  }

  // ---------- Defensive: setParameter with null value but valid JdbcType ----------

  @Test
  public void setParameter_allowsNullValue_whenJdbcTypeIsProvided() throws SQLException {
    StringTypeHandler handler = new StringTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    handler.setParameter(ps, 2, null, JdbcType.VARCHAR);

    // Verify the driver receives a SQL NULL (actual SQL type code depends on the enum).
    verify(ps).setNull(eq(2), eq(JdbcType.VARCHAR.TYPE_CODE));
  }
}