package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test suite for {@link ArrayTypeHandler}.
 * Note: The original test class name 'ArrayTypeHandler_ESTestTest8' was renamed to 'ArrayTypeHandlerTest'
 * to follow standard Java naming conventions.
 */
public class ArrayTypeHandlerTest {

    /**
     * Verifies that calling getNullableResult with a column name throws a NullPointerException
     * when the provided ResultSet is null. This is the expected behavior as the method
     * will attempt to dereference the null ResultSet.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeWhenGettingResultByColumnNameFromNullResultSet() throws SQLException {
        // Arrange
        ArrayTypeHandler handler = new ArrayTypeHandler();
        String anyColumnName = "anyColumn";

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        handler.getNullableResult((ResultSet) null, anyColumnName);
    }
}