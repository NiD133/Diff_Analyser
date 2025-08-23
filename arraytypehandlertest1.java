package org.apache.ibatis.type;

import static org.mockito.Mockito.verify;

import java.sql.Array;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Test suite for {@link ArrayTypeHandler}.
 * This test focuses on the setParameter behavior.
 */
// The class name is improved to follow standard conventions.
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final ArrayTypeHandler TYPE_HANDLER = new ArrayTypeHandler();

  // The mock variable is given a more descriptive name.
  @Mock
  private Array sqlArray;

  /**
   * This test case is mandated by the superclass {@link BaseTypeHandlerTest}.
   * It verifies that when a `java.sql.Array` is passed as a parameter,
   * the handler correctly calls `PreparedStatement.setArray()`.
   */
  @Override
  @Test
  @DisplayName("Should pass a java.sql.Array parameter directly to PreparedStatement")
  void shouldSetParameter() throws Exception {
    // Arrange: The 'ps' (PreparedStatement mock from BaseTypeHandlerTest) and 'sqlArray'
    // mocks are initialized by the test framework. No further arrangement is needed.

    // Act: Call the method under test with the mock SQL array.
    TYPE_HANDLER.setParameter(ps, 1, sqlArray, null);

    // Assert: Verify that the handler delegated the call to the PreparedStatement
    // with the correct arguments.
    verify(ps).setArray(1, sqlArray);
  }
}