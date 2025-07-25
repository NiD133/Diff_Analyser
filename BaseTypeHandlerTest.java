package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Abstract test class for testing implementations of {@link BaseTypeHandler}.
 * This class provides a common setup for mocking SQL-related objects and
 * defines abstract methods for specific test cases that subclasses must implement.
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
   * Test case for setting a parameter in a {@link PreparedStatement}.
   * Subclasses should implement this method to verify the correct behavior
   * of the {@link BaseTypeHandler#setParameter} method.
   */
  public abstract void testSetParameter() throws Exception;

  /**
   * Test case for retrieving a result from a {@link ResultSet} by column name.
   * Subclasses should implement this method to verify the correct behavior
   * of the {@link BaseTypeHandler#getResult(ResultSet, String)} method.
   */
  public abstract void testGetResultFromResultSetByName() throws Exception;

  /**
   * Test case for handling null results from a {@link ResultSet} by column name.
   * Subclasses should implement this method to verify the correct behavior
   * of handling null values in the {@link BaseTypeHandler#getResult(ResultSet, String)} method.
   */
  public abstract void testGetResultNullFromResultSetByName() throws Exception;

  /**
   * Test case for retrieving a result from a {@link ResultSet} by column index.
   * Subclasses should implement this method to verify the correct behavior
   * of the {@link BaseTypeHandler#getResult(ResultSet, int)} method.
   */
  public abstract void testGetResultFromResultSetByPosition() throws Exception;

  /**
   * Test case for handling null results from a {@link ResultSet} by column index.
   * Subclasses should implement this method to verify the correct behavior
   * of handling null values in the {@link BaseTypeHandler#getResult(ResultSet, int)} method.
   */
  public abstract void testGetResultNullFromResultSetByPosition() throws Exception;

  /**
   * Test case for retrieving a result from a {@link CallableStatement}.
   * Subclasses should implement this method to verify the correct behavior
   * of the {@link BaseTypeHandler#getResult(CallableStatement, int)} method.
   */
  public abstract void testGetResultFromCallableStatement() throws Exception;

  /**
   * Test case for handling null results from a {@link CallableStatement}.
   * Subclasses should implement this method to verify the correct behavior
   * of handling null values in the {@link BaseTypeHandler#getResult(CallableStatement, int)} method.
   */
  public abstract void testGetResultNullFromCallableStatement() throws Exception;
}