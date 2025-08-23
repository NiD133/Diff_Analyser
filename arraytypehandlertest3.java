package org.apache.ibatis.type;

import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.sql.Types;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 * This test focuses on the handling of null parameters.
 */
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

  @Test
  void shouldSetNullOnPreparedStatementWhenParameterIsNull() throws SQLException {
    // Arrange: The PreparedStatement 'ps' is a mock provided by the BaseTypeHandlerTest superclass.
    // The parameter to be set is null, which is the scenario under test.

    // Act: Call the setParameter method with a null value.
    TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);

    // Assert: Verify that the type handler correctly calls setNull on the PreparedStatement.
    // The call should use the correct parameter index and the expected JDBC type.
    verify(ps).setNull(1, Types.ARRAY);
  }
}