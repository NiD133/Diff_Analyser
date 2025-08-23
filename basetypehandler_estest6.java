package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.sql.PreparedStatement;
import org.junit.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 * This class focuses on verifying the handler's behavior with different parameters.
 */
public class ArrayTypeHandlerTest {

    /**
     * Verifies that setNonNullParameter throws a TypeException when the parameter
     * is not a Java array or a SQL Array.
     */
    @Test
    public void setNonNullParameterShouldThrowExceptionForUnsupportedParameterType() {
        // Arrange: Set up the handler, a mock statement, and an invalid parameter.
        // The parameter is invalid because ArrayTypeHandler only accepts arrays.
        ArrayTypeHandler handler = new ArrayTypeHandler();
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Object unsupportedParameter = new MonthTypeHandler(); // Any non-array object will trigger the exception.
        int parameterIndex = 1;

        // Act & Assert: Call the method and verify that it throws the correct exception
        // with a descriptive message.
        try {
            handler.setNonNullParameter(mockedStatement, parameterIndex, unsupportedParameter, JdbcType.ARRAY);
            fail("Expected a TypeException to be thrown for an unsupported parameter type.");
        } catch (TypeException e) {
            // This is the expected outcome.
            String expectedMessage = "ArrayType Handler requires SQL array or java array parameter and does not support type "
                + unsupportedParameter.getClass();
            assertEquals(expectedMessage, e.getMessage());
        } catch (Exception e) {
            fail("Caught an unexpected exception of type: " + e.getClass().getSimpleName());
        }
    }
}