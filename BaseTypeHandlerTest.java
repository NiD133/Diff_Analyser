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
 * Base test class for testing TypeHandler implementations.
 * 
 * TypeHandlers in MyBatis are responsible for converting between Java types and JDBC types.
 * This abstract class provides a common test structure that all TypeHandler tests should follow
 * to ensure consistent testing of:
 * - Setting parameters on PreparedStatements
 * - Getting results from ResultSets (by name and position)
 * - Getting results from CallableStatements
 * - Handling NULL values in all scenarios
 * 
 * Concrete test classes should extend this class and implement all abstract methods
 * to test their specific TypeHandler implementation.
 */
@ExtendWith(MockitoExtension.class)
abstract class BaseTypeHandlerTest {

  // Mock JDBC objects used across all TypeHandler tests
  @Mock
  protected ResultSet rs;
  
  @Mock
  protected PreparedStatement ps;
  
  @Mock
  protected CallableStatement cs;
  
  @Mock
  protected ResultSetMetaData rsmd;

  // ========== Parameter Setting Tests ==========
  
  /**
   * Test that the TypeHandler correctly sets a non-null parameter value
   * on a PreparedStatement at the specified position.
   */
  public abstract void shouldSetParameter() throws Exception;

  // ========== ResultSet Tests (By Column Name) ==========
  
  /**
   * Test that the TypeHandler correctly retrieves a non-null value
   * from a ResultSet using the column name.
   */
  public abstract void shouldGetResultFromResultSetByName() throws Exception;

  /**
   * Test that the TypeHandler correctly handles NULL values
   * when retrieving from a ResultSet using the column name.
   */
  public abstract void shouldGetResultNullFromResultSetByName() throws Exception;

  // ========== ResultSet Tests (By Column Index) ==========
  
  /**
   * Test that the TypeHandler correctly retrieves a non-null value
   * from a ResultSet using the column index (position).
   */
  public abstract void shouldGetResultFromResultSetByPosition() throws Exception;

  /**
   * Test that the TypeHandler correctly handles NULL values
   * when retrieving from a ResultSet using the column index.
   */
  public abstract void shouldGetResultNullFromResultSetByPosition() throws Exception;

  // ========== CallableStatement Tests ==========
  
  /**
   * Test that the TypeHandler correctly retrieves a non-null value
   * from a CallableStatement (stored procedure OUT parameter).
   */
  public abstract void shouldGetResultFromCallableStatement() throws Exception;

  /**
   * Test that the TypeHandler correctly handles NULL values
   * when retrieving from a CallableStatement.
   */
  public abstract void shouldGetResultNullFromCallableStatement() throws Exception;
}