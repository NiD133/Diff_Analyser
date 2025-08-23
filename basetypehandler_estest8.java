package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.CallableStatement;
import org.apache.ibatis.executor.result.ResultMapException;
import org.junit.Test;

/**
 * Test suite for the abstract BaseTypeHandler class.
 */
public class BaseTypeHandlerTest {

  // Use a concrete implementation (ClobTypeHandler) to test the abstract class's logic.
  private final BaseTypeHandler<String> typeHandler = ClobTypeHandler.INSTANCE;

  /**
   * Verifies that getResult(CallableStatement, int) wraps any underlying exception
   * in a descriptive ResultMapException. This is tested by passing a null statement,
   * which is expected to cause a NullPointerException internally.
   */
  @Test
  public void shouldWrapExceptionWhenGettingResultFromNullCallableStatement() {
    // Arrange
    int columnIndex = 4491;
    String expectedErrorMessage = "Error attempting to get column #" + columnIndex
        + " from callable statement.  Cause: java.lang.NullPointerException";

    try {
      // Act
      typeHandler.getResult((CallableStatement) null, columnIndex);
      fail("Expected a ResultMapException to be thrown, but nothing was thrown.");
    } catch (ResultMapException e) {
      // Assert
      assertEquals(expectedErrorMessage, e.getMessage());
      assertNotNull("The wrapped cause should not be null.", e.getCause());
      assertTrue("The cause should be a NullPointerException.",
          e.getCause() instanceof NullPointerException);
    } catch (Exception e) {
      fail("An unexpected exception type was thrown: " + e.getClass().getName());
    }
  }
}