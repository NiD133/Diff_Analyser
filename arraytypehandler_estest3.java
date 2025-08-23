package org.apache.ibatis.type;

import static org.mockito.Mockito.mock;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test suite for {@link ArrayTypeHandler}.
 *
 * This class focuses on testing the behavior of the ArrayTypeHandler,
 * particularly its handling of parameter types.
 */
public class ArrayTypeHandlerTest {

    // The ExpectedException rule allows for more readable exception testing in JUnit 4.
    // It clearly states the expected exception type and message before the action is performed.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

    /**
     * Verifies that setNonNullParameter throws a RuntimeException when the parameter
     * is not a supported array type.
     *
     * The original test name "test02" was not descriptive. This name clearly states
     * the method under test, the expected outcome, and the condition that causes it.
     */
    @Test
    public void setNonNullParameterShouldThrowExceptionForUnsupportedType() throws SQLException {
        // Arrange
        // Use descriptive variable names instead of "preparedStatement0" or "time0".
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        // The handler is designed for arrays. We use java.sql.Time as an example of an
        // unsupported, non-array type to trigger the exception.
        Time unsupportedParameter = new Time(0L);

        // Use a conventional index '1' instead of a magic number like '1577'.
        int parameterIndex = 1;

        // The specific JdbcType is not critical for this test's logic.
        JdbcType jdbcType = JdbcType.BINARY;

        // Assert - Define expectations for the exception.
        // This approach is cleaner and more declarative than a try-catch block with fail().
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("ArrayType Handler requires SQL array or java array parameter and does not support type class java.sql.Time");

        // Act - Call the method under test.
        // This call is expected to throw the configured exception, causing the test to pass.
        arrayTypeHandler.setNonNullParameter(mockStatement, parameterIndex, unsupportedParameter, jdbcType);
    }
}