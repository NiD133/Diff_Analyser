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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Provides a base contract and common mocks for testing {@link TypeHandler} implementations.
 * <p>
 * Subclasses should extend this class and implement the abstract test methods to ensure
 * their custom {@link TypeHandler} behaves as expected.
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
   * Tests for {@link TypeHandler#setParameter(PreparedStatement, int, Object, JdbcType)}.
   */
  @Nested
  @DisplayName("Setting Parameters")
  class SetParameterTests {

    @Test
    @DisplayName("should set a non-null parameter on a PreparedStatement")
    public abstract void shouldSetParameter() throws Exception;
  }

  /**
   * Tests for {@link TypeHandler#getResult(ResultSet, String)} and
   * {@link TypeHandler#getResult(ResultSet, int)}.
   */
  @Nested
  @DisplayName("Getting Results from ResultSet")
  class GetResultFromResultSetTests {

    @Test
    @DisplayName("should get a non-null result by column name")
    public abstract void shouldGetResultFromResultSetByName() throws Exception;

    @Test
    @DisplayName("should get a null result by column name when value is SQL NULL")
    public abstract void shouldGetResultNullFromResultSetByName() throws Exception;

    @Test
    @DisplayName("should get a non-null result by column index")
    public abstract void shouldGetResultFromResultSetByPosition() throws Exception;

    @Test
    @DisplayName("should get a null result by column index when value is SQL NULL")
    public abstract void shouldGetResultNullFromResultSetByPosition() throws Exception;
  }

  /**
   * Tests for {@link TypeHandler#getResult(CallableStatement, int)}.
   */
  @Nested
  @DisplayName("Getting Results from CallableStatement")
  class GetResultFromCallableStatementTests {

    @Test
    @DisplayName("should get a non-null result by column index")
    public abstract void shouldGetResultFromCallableStatement() throws Exception;

    @Test
    @DisplayName("should get a null result by column index when value is SQL NULL")
    public abstract void shouldGetResultNullFromCallableStatement() throws Exception;
  }
}