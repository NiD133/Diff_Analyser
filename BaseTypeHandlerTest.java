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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Base support for {@link TypeHandler} tests.
 * <p>
 * This abstract class centralizes common JDBC mocks and establishes a small
 * vocabulary (column name and index) so that concrete test classes can focus
 * on the behavior of a specific {@link TypeHandler} implementation rather than
 * on boilerplate setup.
 *
 * What this class provides:
 * - Ready-to-use Mockito mocks for ResultSet, PreparedStatement, CallableStatement and ResultSetMetaData.
 * - Conventional column identifiers: COLUMN_NAME ("col") and COLUMN_INDEX (1).
 * - A set of abstract test methods that concrete tests should implement to cover the typical
 *   interactions of a {@link TypeHandler}:
 *   - Setting a parameter on a PreparedStatement.
 *   - Reading a value from a ResultSet by column name and by column index (both non-null and null cases).
 *   - Reading a value from a CallableStatement by column index (both non-null and null cases).
 *
 * Notes for implementors:
 * - Prefer describing test intent in Given/When/Then style within each test method.
 * - Use COLUMN_NAME and COLUMN_INDEX consistently to minimize cognitive overhead.
 * - The base {@link BaseTypeHandler} does not call wasNull() since 3.5.0; null handling is up to the
 *   concrete handler implementation. Stub your mocks accordingly.
 *
 * Example (sketch):
 * /*
 * class StringTypeHandlerTest extends BaseTypeHandlerTest {
 *   private final StringTypeHandler handler = new StringTypeHandler();
 *
 *   @Test
 *   @Override
 *   public void shouldSetParameter() throws Exception {
 *     // Given
 *     String value = "abc";
 *     // When
 *     handler.setParameter(ps, 1, value, JdbcType.VARCHAR);
 *     // Then
 *     verify(ps).setString(1, "abc");
 *   }
 *
 *   @Test
 *   @Override
 *   public void shouldGetResultFromResultSetByName() throws Exception {
 *     when(rs.getString(COLUMN_NAME)).thenReturn("abc");
 *     assertEquals("abc", handler.getResult(rs, COLUMN_NAME));
 *   }
 *
 *   // ...and so on for the remaining abstract tests
 * }
 * *\/
 */
@ExtendWith(MockitoExtension.class)
abstract class BaseTypeHandlerTest {

  /**
   * Conventional column name used across tests to reduce duplication and improve readability.
   */
  protected static final String COLUMN_NAME = "col";

  /**
   * Conventional column index used across tests to reduce duplication and improve readability.
   */
  protected static final int COLUMN_INDEX = 1;

  // Common JDBC test doubles
  @Mock
  protected ResultSet rs;

  @Mock
  protected PreparedStatement ps;

  @Mock
  protected CallableStatement cs;

  @Mock
  protected ResultSetMetaData rsmd;

  /**
   * Verifies that the handler sets a non-null parameter on a PreparedStatement using the appropriate JDBC API,
   * respecting the provided JdbcType when necessary.
   * Typical expectations:
   * - Non-null values are delegated to setNonNullParameter(...).
   * - Null values are handled according to the handler’s contract (may use setNull or other strategy).
   */
  public abstract void shouldSetParameter() throws Exception;

  /**
   * Verifies that the handler correctly maps a non-null database value from a ResultSet when accessed by column name.
   * Use COLUMN_NAME for consistency.
   */
  public abstract void shouldGetResultFromResultSetByName() throws Exception;

  /**
   * Verifies that the handler correctly maps a SQL NULL from a ResultSet when accessed by column name.
   * Use COLUMN_NAME for consistency.
   */
  public abstract void shouldGetResultNullFromResultSetByName() throws Exception;

  /**
   * Verifies that the handler correctly maps a non-null database value from a ResultSet when accessed by column index.
   * Use COLUMN_INDEX for consistency.
   */
  public abstract void shouldGetResultFromResultSetByPosition() throws Exception;

  /**
   * Verifies that the handler correctly maps a SQL NULL from a ResultSet when accessed by column index.
   * Use COLUMN_INDEX for consistency.
   */
  public abstract void shouldGetResultNullFromResultSetByPosition() throws Exception;

  /**
   * Verifies that the handler correctly maps a non-null database value from a CallableStatement (OUT parameter)
   * when accessed by column index. Use COLUMN_INDEX for consistency.
   */
  public abstract void shouldGetResultFromCallableStatement() throws Exception;

  /**
   * Verifies that the handler correctly maps a SQL NULL from a CallableStatement (OUT parameter)
   * when accessed by column index. Use COLUMN_INDEX for consistency.
   */
  public abstract void shouldGetResultNullFromCallableStatement() throws Exception;
}