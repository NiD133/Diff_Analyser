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
 * Abstract base class for testing {@link BaseTypeHandler} implementations.
 * Provides common setup and utility methods for subclasses.
 */
@ExtendWith(MockitoExtension.class)
abstract class BaseTypeHandlerTest {

  // Mocked SQL objects to be used in test cases
  @Mock
  protected ResultSet resultSet;
  
  @Mock
  protected PreparedStatement preparedStatement;
  
  @Mock
  protected CallableStatement callableStatement;
  
  @Mock
  protected ResultSetMetaData resultSetMetaData;

  /**
   * Test the setting of a parameter in a PreparedStatement.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testSetParameter() throws Exception;

  /**
   * Test retrieving a result from a ResultSet by column name.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testGetResultFromResultSetByName() throws Exception;

  /**
   * Test retrieving a null result from a ResultSet by column name.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testGetNullResultFromResultSetByName() throws Exception;

  /**
   * Test retrieving a result from a ResultSet by column index.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testGetResultFromResultSetByPosition() throws Exception;

  /**
   * Test retrieving a null result from a ResultSet by column index.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testGetNullResultFromResultSetByPosition() throws Exception;

  /**
   * Test retrieving a result from a CallableStatement.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testGetResultFromCallableStatement() throws Exception;

  /**
   * Test retrieving a null result from a CallableStatement.
   * 
   * @throws Exception if an error occurs during the test
   */
  public abstract void testGetNullResultFromCallableStatement() throws Exception;
}