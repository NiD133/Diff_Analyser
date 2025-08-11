package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnumOrdinalTypeHandlerTest {

  private EnumOrdinalTypeHandler<JdbcType> newHandler() {
    return new EnumOrdinalTypeHandler<>(JdbcType.class);
  }

  // Constructor

  @Test
  public void constructor_shouldRejectNullType() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> new EnumOrdinalTypeHandler<>(null));
    assertEquals("Type argument cannot be null", ex.getMessage());
  }

  // setNonNullParameter

  @Test
  public void setNonNullParameter_shouldWriteEnumOrdinalToPreparedStatement() throws SQLException {
    PreparedStatement ps = mock(PreparedStatement.class);
    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();

    handler.setNonNullParameter(ps, 5, JdbcType.LONGVARBINARY, JdbcType.INTEGER);

    verify(ps).setInt(5, JdbcType.LONGVARBINARY.ordinal());
    verifyNoMoreInteractions(ps);
  }

  // ResultSet by column index

  @Test
  public void getNullableResult_byIndex_whenWasNull_returnsNull() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt(7)).thenReturn(0);
    when(rs.wasNull()).thenReturn(true);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();
    JdbcType result = handler.getNullableResult(rs, 7);

    assertNull(result);
  }

  @Test
  public void getNullableResult_byIndex_withValidOrdinal_returnsEnum() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt(7)).thenReturn(1); // JdbcType.BIT has ordinal 1
    when(rs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();
    JdbcType result = handler.getNullableResult(rs, 7);

    assertEquals(JdbcType.BIT, result);
  }

  @Test
  public void getNullableResult_byIndex_withNegativeOrdinal_throwsIAE() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt(3)).thenReturn(-1);
    when(rs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> handler.getNullableResult(rs, 3));

    assertTrue(ex.getMessage().contains("Cannot convert -1 to JdbcType by ordinal value."));
  }

  @Test
  public void getNullableResult_byIndex_withTooLargeOrdinal_throwsIAE() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt(2)).thenReturn(9999);
    when(rs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> handler.getNullableResult(rs, 2));

    assertTrue(ex.getMessage().contains("Cannot convert 9999 to JdbcType by ordinal value."));
  }

  // ResultSet by column name

  @Test
  public void getNullableResult_byName_withValidOrdinal_returnsEnum() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt("col")).thenReturn(0); // JdbcType.ARRAY has ordinal 0
    when(rs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();
    JdbcType result = handler.getNullableResult(rs, "col");

    assertEquals(JdbcType.ARRAY, result);
  }

  @Test
  public void getNullableResult_byName_whenWasNull_returnsNull() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt("col")).thenReturn(0);
    when(rs.wasNull()).thenReturn(true);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();
    JdbcType result = handler.getNullableResult(rs, "col");

    assertNull(result);
  }

  @Test
  public void getNullableResult_byName_withNegativeOrdinal_throwsIAE() throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt("col")).thenReturn(-874);
    when(rs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> handler.getNullableResult(rs, "col"));

    assertTrue(ex.getMessage().contains("Cannot convert -874 to JdbcType by ordinal value."));
  }

  // CallableStatement by column index

  @Test
  public void getNullableResult_fromCallable_withValidOrdinal_returnsEnum() throws SQLException {
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getInt(4)).thenReturn(0); // JdbcType.ARRAY
    when(cs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();
    JdbcType result = handler.getNullableResult(cs, 4);

    assertEquals(JdbcType.ARRAY, result);
  }

  @Test
  public void getNullableResult_fromCallable_whenWasNull_returnsNull() throws SQLException {
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getInt(4)).thenReturn(0);
    when(cs.wasNull()).thenReturn(true);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();
    JdbcType result = handler.getNullableResult(cs, 4);

    assertNull(result);
  }

  @Test
  public void getNullableResult_fromCallable_withInvalidOrdinal_throwsIAE() throws SQLException {
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getInt(1)).thenReturn(-1824);
    when(cs.wasNull()).thenReturn(false);

    EnumOrdinalTypeHandler<JdbcType> handler = newHandler();

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> handler.getNullableResult(cs, 1));

    assertTrue(ex.getMessage().contains("Cannot convert -1824 to JdbcType by ordinal value."));
  }
}

What changed and why:
- Removed EvoSuite runner and scaffolding to focus on behavior, not framework internals.
- Gave tests descriptive names that explain intent and behavior.
- Grouped tests by method and scenario (constructor, PreparedStatement, ResultSet by index/name, CallableStatement).
- Used small, well-known ordinals (0 -> ARRAY, 1 -> BIT) to avoid fragile assertions.
- Verified PreparedStatement receives the enum ordinal.
- Asserted exception messages contain the key information without overfitting exact formatting.
- Avoided null ResultSet/CallableStatement tests that only trigger trivial NPEs, keeping the suite focused on handler behavior.