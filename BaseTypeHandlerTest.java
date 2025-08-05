/*
 *    Copyright 2009-2022 the original author or authors.
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
 * Base test class for {@link TypeHandler} implementations.
 * <p>
 * Provides common mock objects for testing JDBC interactions and defines the required test
 * methods that concrete subclasses must implement to validate specific type handlers.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
abstract class BaseTypeHandlerTest {

  @Mock
  protected ResultSet rs;
  @Mock
  protected PreparedStatement ps;
  @Mock
  protected CallableStatement cs;
  @Mock
  protected ResultSetMetaData rsmd;

  /**
   * Tests setting a non-null parameter on a {@link PreparedStatement}.
   * <p>
   * Concrete implementations should verify that the type handler correctly sets the parameter
   * value at the specified position using the appropriate JDBC type.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldSetParameter() throws Exception;

  /**
   * Tests retrieving a non-null result from a {@link ResultSet} using a column name.
   * <p>
   * Concrete implementations should verify that the type handler correctly reads and converts
   * a non-null value from the specified column.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldGetResultFromResultSetByName() throws Exception;

  /**
   * Tests retrieving a null result from a {@link ResultSet} using a column name.
   * <p>
   * Concrete implementations should verify that the type handler correctly handles SQL {@code NULL}
   * values and returns a Java {@code null} or equivalent representation.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldGetResultNullFromResultSetByName() throws Exception;

  /**
   * Tests retrieving a non-null result from a {@link ResultSet} using a column index.
   * <p>
   * Concrete implementations should verify that the type handler correctly reads and converts
   * a non-null value from the specified column position.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldGetResultFromResultSetByPosition() throws Exception;

  /**
   * Tests retrieving a null result from a {@link ResultSet} using a column index.
   * <p>
   * Concrete implementations should verify that the type handler correctly handles SQL {@code NULL}
   * values and returns a Java {@code null} or equivalent representation.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldGetResultNullFromResultSetByPosition() throws Exception;

  /**
   * Tests retrieving a non-null result from a {@link CallableStatement} using a column index.
   * <p>
   * Concrete implementations should verify that the type handler correctly reads and converts
   * a non-null value from the specified output parameter or result column.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldGetResultFromCallableStatement() throws Exception;

  /**
   * Tests retrieving a null result from a {@link CallableStatement} using a column index.
   * <p>
   * Concrete implementations should verify that the type handler correctly handles SQL {@code NULL}
   * values and returns a Java {@code null} or equivalent representation.
   * </p>
   *
   * @throws Exception if any error occurs during the test
   */
  public abstract void shouldGetResultNullFromCallableStatement() throws Exception;
}