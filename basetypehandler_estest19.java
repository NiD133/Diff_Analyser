package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import org.apache.ibatis.executor.result.ResultMapException;
import org.junit.Test;

/**
 * Test suite for the {@link BaseTypeHandler}.
 */
public class BaseTypeHandlerTest {

    /**
     * Tests that getResult(ResultSet, int) throws a descriptive exception
     * when the provided ResultSet is null.
     *
     * The BaseTypeHandler is expected to catch the underlying NullPointerException
     * and wrap it in a more informative RuntimeException, guiding the user
     * to the source of the error.
     */
    @Test
    public void getResultByColumnIndexShouldThrowExceptionWhenResultSetIsNull() {
        // Arrange
        // Use a concrete implementation (IntegerTypeHandler) to test the abstract BaseTypeHandler's logic.
        BaseTypeHandler<Integer> typeHandler = IntegerTypeHandler.INSTANCE;
        int columnIndex = 1;

        // Act & Assert
        try {
            // Attempt to get a result from a null ResultSet.
            typeHandler.getResult((ResultSet) null, columnIndex);
            fail("Should have thrown a RuntimeException because the ResultSet is null.");
        } catch (RuntimeException e) {
            // The actual exception thrown by MyBatis is ResultMapException, a subclass of RuntimeException.
            // This check ensures the exception is the expected wrapper.
            String expectedMessage = "Error attempting to get column #" + columnIndex + " from result set.  Cause: java.lang.NullPointerException";
            assertEquals(expectedMessage, e.getMessage());

            // Verify that the cause of the failure was the expected NullPointerException.
            assertNotNull("Exception cause should not be null.", e.getCause());
            assertTrue("Exception cause should be a NullPointerException.", e.getCause() instanceof NullPointerException);
        } catch (Exception e) {
            fail("Caught an unexpected exception type: " + e.getClass().getName());
        }
    }
}