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
 * Base test class for type handlers.
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseTypeHandlerTest {

  @Mock
  protected ResultSet mockResultSet;

  @Mock
  protected PreparedStatement mockPreparedStatement;

  @Mock
  protected CallableStatement mockCallableStatement;

  @Mock
  protected ResultSetMetaData mockResultSetMetaData;

  /**
   * Tests setting a parameter in a prepared statement.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testSetParameter() throws Exception;

  /**
   * Tests getting a result from a result set by column name.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testGetResultFromResultSetByColumnName() throws Exception;

  /**
   * Tests getting a null result from a result set by column name.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testGetNullResultFromResultSetByColumnName() throws Exception;

  /**
   * Tests getting a result from a result set by column index.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testGetResultFromResultSetByColumnIndex() throws Exception;

  /**
   * Tests getting a null result from a result set by column index.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testGetNullResultFromResultSetByColumnIndex() throws Exception;

  /**
   * Tests getting a result from a callable statement.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testGetResultFromCallableStatement() throws Exception;

  /**
   * Tests getting a null result from a callable statement.
   * 
   * @throws Exception if an error occurs during testing
   */
  public abstract void testGetNullResultFromCallableStatement() throws Exception;
}