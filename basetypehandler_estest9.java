package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test suite for BaseTypeHandler and its subclasses.
 */
public class BaseTypeHandlerTest {

    /**
     * Verifies that getNullableResult(ResultSet, String) throws a NullPointerException
     * when the provided ResultSet is null. The handler is not expected to handle a null ResultSet.
     */
    @Test(expected = NullPointerException.class)
    public void getNullableResultByColumnNameShouldThrowNpeForNullResultSet() throws SQLException {
        // Arrange
        // Using ObjectTypeHandler as a concrete implementation of the abstract BaseTypeHandler.
        ObjectTypeHandler objectTypeHandler = new ObjectTypeHandler();
        String anyColumnName = "anyColumn";

        // Act & Assert
        // This call is expected to throw a NullPointerException because the ResultSet is null.
        objectTypeHandler.getNullableResult((ResultSet) null, anyColumnName);
    }
}