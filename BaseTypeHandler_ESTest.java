/*
 *    Copyright 2009-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.type;

import org.apache.ibatis.session.Configuration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseTypeHandler}.
 *
 * This test suite focuses on the common behavior provided by the abstract BaseTypeHandler,
 * such as null handling in setParameter and exception wrapping.
 * Concrete subclasses (e.g., StringTypeHandler, ClobTypeHandler) are used to instantiate
 * and test these abstract behaviors.
 */
public class BaseTypeHandlerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  // =================================================================================
  // Tests for setParameter(PreparedStatement, int, T, JdbcType)
  // =================================================================================

  @Test
  public void setParameterShouldSucceedForNonNullValue() throws SQLException {
    // Arrange
    StringTypeHandler handler = new StringTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    // Act & Assert (should not throw)
    handler.setParameter(ps, 1, "test", JdbcType.VARCHAR);
  }

  @Test
  public void setParameterShouldSucceedWhenParameterIsNullAndJdbcTypeIsProvided() throws SQLException {
    // Arrange
    ClobTypeHandler handler = new ClobTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    // Act & Assert (should not throw)
    handler.setParameter(ps, 1, null, JdbcType.CLOB);
  }

  @Test
  public void setParameterShouldThrowTypeExceptionWhenParameterIsNullAndJdbcTypeIsNull() throws SQLException {
    // Arrange
    ClobTypeHandler handler = new ClobTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);

    // Assert
    thrown.expect(TypeException.class);
    thrown.expectMessage("JDBC requires that the JdbcType must be specified for all nullable parameters.");

    // Act
    handler.setParameter(ps, 1, null, null);
  }

  @Test
  public void setParameterShouldWrapExceptionWhenSettingNonNullParameterFails() throws SQLException {
    // Arrange
    // Using a null PreparedStatement will cause a NullPointerException internally
    ClobTypeHandler handler = ClobTypeHandler.INSTANCE;

    // Assert
    thrown.expect(TypeException.class);
    thrown.expectMessage("Error setting non null for parameter #1 with JdbcType STRUCT");
    thrown.expectCause(org.hamcrest.Matchers.isA(NullPointerException.class));

    // Act
    handler.setParameter(null, 1, "some value", JdbcType.STRUCT);
  }

  // =================================================================================
  // Tests for getResult(...)
  // =================================================================================

  @Test
  public void getResultFromResultSetByIndexShouldReturnExpectedValue() throws SQLException {
    // Arrange
    IntegerTypeHandler handler = new IntegerTypeHandler();
    ResultSet rs = mock(ResultSet.class);
    when(rs.getInt(1)).thenReturn(100);
    when(rs.wasNull()).thenReturn(false);

    // Act
    Integer result = handler.getResult(rs, 1);

    // Assert
    assertEquals(Integer.valueOf(100), result);
  }

  @Test
  public void getResultFromResultSetByNameShouldReturnExpectedValue() throws SQLException {
    // Arrange
    StringTypeHandler handler = new StringTypeHandler();
    ResultSet rs = mock(ResultSet.class);
    when(rs.getString("columnName")).thenReturn("test");
    when(rs.wasNull()).thenReturn(false);

    // Act
    String result = handler.getResult(rs, "columnName");

    // Assert
    assertEquals("test", result);
  }

  @Test
  public void getResultFromCallableStatementShouldReturnExpectedValue() throws SQLException {
    // Arrange
    StringTypeHandler handler = new StringTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getString(1)).thenReturn("test");
    when(cs.wasNull()).thenReturn(false);

    // Act
    String result = handler.getResult(cs, 1);

    // Assert
    assertEquals("test", result);
  }

  @Test
  public void getResultFromCallableStatementShouldReturnNullWhenDatabaseValueIsNull() throws SQLException {
    // Arrange
    YearMonthTypeHandler handler = new YearMonthTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getString(anyInt())).thenReturn(null);

    // Act
    YearMonth result = handler.getResult(cs, 1);

    // Assert
    assertNull(result);
  }

  @Test
  public void getResultFromResultSetByIndexShouldWrapExceptionOnFailure() throws SQLException {
    // Arrange
    IntegerTypeHandler handler = IntegerTypeHandler.INSTANCE;

    // Assert
    thrown.expect(ResultMapException.class);
    thrown.expectMessage("Error attempting to get column #1 from result set.  Cause: java.lang.NullPointerException");

    // Act
    // Pass a null ResultSet to trigger an internal NullPointerException
    handler.getResult((ResultSet) null, 1);
  }

  @Test
  public void getResultFromResultSetByNameShouldWrapExceptionOnFailure() throws SQLException {
    // Arrange
    ObjectTypeHandler handler = ObjectTypeHandler.INSTANCE;

    // Assert
    thrown.expect(ResultMapException.class);
    thrown.expectMessage("Error attempting to get column 'columnName' from result set.  Cause: java.lang.NullPointerException");

    // Act
    handler.getResult((ResultSet) null, "columnName");
  }

  @Test
  public void getResultFromCallableStatementShouldWrapExceptionOnFailure() throws SQLException {
    // Arrange
    ClobTypeHandler handler = ClobTypeHandler.INSTANCE;

    // Assert
    thrown.expect(ResultMapException.class);
    thrown.expectMessage("Error attempting to get column #1 from callable statement.  Cause: java.lang.NullPointerException");

    // Act
    handler.getResult((CallableStatement) null, 1);
  }

  // =================================================================================
  // Tests for getNullableResult(...)
  // =================================================================================

  @Test
  public void getNullableResultFromCallableStatementShouldReturnNullWhenDatabaseValueIsNull() throws SQLException {
    // Arrange
    ObjectTypeHandler handler = new ObjectTypeHandler();
    CallableStatement cs = mock(CallableStatement.class);
    doReturn(null).when(cs).getObject(anyInt());

    // Act
    Object result = handler.getNullableResult(cs, 1);

    // Assert
    assertNull(result);
  }

  // =================================================================================
  // Other tests
  // =================================================================================

  @Test
  @SuppressWarnings("deprecation")
  public void setConfigurationShouldNotThrowException() {
    // Arrange
    ClobTypeHandler handler = new ClobTypeHandler();

    // Act & Assert (should not throw)
    // This method is deprecated and its implementation is empty.
    handler.setConfiguration(new Configuration());
  }
}