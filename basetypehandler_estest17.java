package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import org.apache.ibatis.executor.result.ResultMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the BaseTypeHandler class.
 * This focuses on the common exception handling logic in the base class.
 */
class BaseTypeHandlerTest {

    // Using a concrete implementation for testing the abstract BaseTypeHandler
    private BaseTypeHandler<Object> typeHandler;

    @BeforeEach
    void setUp() {
        typeHandler = new ObjectTypeHandler();
    }

    /**
     * Verifies that getResult(ResultSet, String) wraps any exception
     * in a descriptive ResultMapException.
     */
    @Test
    void getResultByColumnNameShouldThrowDescriptiveExceptionOnNullResultSet() {
        // Arrange
        String columnName = "anyColumn";
        // The condition under test is the null ResultSet.

        // Act & Assert
        // We expect a ResultMapException that wraps the underlying NullPointerException.
        ResultMapException thrown = assertThrows(ResultMapException.class, () -> {
            typeHandler.getResult((ResultSet) null, columnName);
        });

        // Verify the exception details for better diagnostics and to confirm the wrapping logic.
        assertTrue(thrown.getMessage().contains("Error attempting to get column 'anyColumn' from result set."));
        assertTrue(thrown.getCause() instanceof NullPointerException, "The cause of the exception should be a NullPointerException.");
    }
}