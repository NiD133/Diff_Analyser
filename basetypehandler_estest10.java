package org.apache.ibatis.type;

import org.junit.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test suite for the IntegerTypeHandler class.
 */
public class IntegerTypeHandlerTest {

    private final IntegerTypeHandler typeHandler = new IntegerTypeHandler();

    /**
     * Verifies that getNullableResult throws a NullPointerException if the ResultSet is null.
     * <p>
     * The underlying implementation is expected to attempt to access the null ResultSet,
     * which should result in an NPE. This test confirms that contract.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenGettingResultByColumnIndexFromNullResultSet() throws SQLException {
        // Calling getNullableResult with a null ResultSet should fail fast.
        // The column index (1) is arbitrary as the exception should be thrown before it's used.
        typeHandler.getNullableResult((ResultSet) null, 1);
    }
}