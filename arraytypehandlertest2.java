package org.apache.ibatis.type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ArrayTypeHandler}.
 *
 * The class name has been corrected from ArrayTypeHandlerTestTest2 to ArrayTypeHandlerTest
 * for clarity and to follow standard naming conventions.
 */
@DisplayName("ArrayTypeHandler Test")
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

  // The unused @Mock field 'mockArray' has been removed to avoid confusion.

  @Test
  @DisplayName("Should set a non-null String array as a parameter on a PreparedStatement")
  void shouldSetStringArrayParameter() throws SQLException {
    // Arrange
    // 1. Define the input data and expected values for clarity.
    final int parameterIndex = 1;
    final String[] inputArray = { "Hello World" };
    // The ArrayTypeHandler resolves the array component type (String) to its corresponding SQL type name.
    // For String.class, this is "VARCHAR".
    final String expectedSqlTypeName = "VARCHAR";

    // 2. Mock the necessary JDBC objects.
    final Array mockSqlArray = mock(Array.class);
    final Connection mockConnection = mock(Connection.class);

    // 3. Stub the mock interactions.
    // The handler needs a connection from the prepared statement to create a SQL Array.
    when(ps.getConnection()).thenReturn(mockConnection);
    // The handler should create a SQL Array with the correct type name and data.
    // Using specific arguments instead of 'any()' makes the test's intent more explicit.
    when(mockConnection.createArrayOf(expectedSqlTypeName, inputArray)).thenReturn(mockSqlArray);

    // Act
    // Call the method under test.
    TYPE_HANDLER.setParameter(ps, parameterIndex, inputArray, JdbcType.ARRAY);

    // Assert
    // Verify that the PreparedStatement's setArray method was called with the correct index and the created SQL Array.
    verify(ps).setArray(parameterIndex, mockSqlArray);
    // Verify that the handler correctly releases the resources of the temporary SQL Array after setting it.
    verify(mockSqlArray).free();
  }
}