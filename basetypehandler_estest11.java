package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.junit.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 * This test suite verifies the behavior of the ArrayTypeHandler, particularly its handling of null inputs.
 */
public class ArrayTypeHandlerTest {

  /**
   * Verifies that getNullableResult throws a NullPointerException if the provided CallableStatement is null.
   * <p>
   * The underlying implementation is expected to interact with the statement object directly,
   * thus a null statement should result in an immediate NullPointerException.
   */
  @Test(expected = NullPointerException.class)
  public void getNullableResultFromCallableStatementShouldThrowNpeForNullStatement() throws SQLException {
    // Arrange
    ArrayTypeHandler handler = new ArrayTypeHandler();
    int anyColumnIndex = 1; // The column index is arbitrary for this test.

    // Act & Assert
    // The following call is expected to throw a NullPointerException.
    handler.getNullableResult((CallableStatement) null, anyColumnIndex);
  }
}