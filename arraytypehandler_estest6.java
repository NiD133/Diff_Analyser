package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Test suite for {@link ArrayTypeHandler}.
 * This test focuses on handling null inputs for JDBC objects.
 */
public class ArrayTypeHandlerTest {

  private final ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

  /**
   * Verifies that getNullableResult throws a NullPointerException when the
   * provided CallableStatement is null. This is the expected behavior, as
   * the method cannot retrieve a result from a null object.
   */
  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenGettingResultFromNullCallableStatement() throws SQLException {
    // The column index is arbitrary because the statement itself is null.
    int anyColumnIndex = 1;
    arrayTypeHandler.getNullableResult((CallableStatement) null, anyColumnIndex);
  }
}