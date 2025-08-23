package org.apache.ibatis.type;

import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Test suite for the {@link BaseTypeHandler}.
 * This class focuses on the generic behavior implemented in the abstract base class.
 */
class BaseTypeHandlerTest {

  // We use a concrete subclass (ClobTypeHandler) to test the functionality
  // of the abstract BaseTypeHandler.
  private final BaseTypeHandler<String> typeHandler = new ClobTypeHandler();

  /**
   * Verifies that setParameter throws a TypeException when the parameter value is null
   * and the JDBC type is not specified. This is a requirement from the JDBC specification,
   * as the driver needs a type hint to handle SQL NULL correctly.
   */
  @Test
  void shouldThrowExceptionWhenSettingNullParameterWithoutJdbcType() {
    // Arrange
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    int anyColumnIndex = 1;
    String nullParameter = null;
    JdbcType nullJdbcType = null;

    // Act & Assert
    // Expect a TypeException because a null parameter requires a non-null JdbcType.
    TypeException thrown = assertThrows(
        TypeException.class,
        () -> typeHandler.setParameter(preparedStatement, anyColumnIndex, nullParameter, nullJdbcType)
    );

    // Verify the exception message is clear and accurate.
    assertEquals("JDBC requires that the JdbcType must be specified for all nullable parameters.", thrown.getMessage());
  }
}