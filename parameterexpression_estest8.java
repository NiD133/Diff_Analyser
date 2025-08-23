package org.apache.ibatis.builder;

import org.apache.ibatis.builder.BuilderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link ParameterExpression}.
 * This refactored test replaces an auto-generated EvoSuite test.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that the ParameterExpression constructor throws a BuilderException
     * when an expression ends with a colon (':') but does not specify a JDBC type.
     * The colon is a special character that signals the start of a JDBC type definition.
     */
    @Test
    public void shouldThrowExceptionForExpressionWithUnspecifiedJdbcType() {
        // Arrange: An expression that is malformed because it ends with a colon,
        // implying a JDBC type is next, but provides nothing.
        String malformedExpression = "property:";
        String expectedErrorMessage = "Parsing error in {property:} in position 9";

        // Act & Assert
        try {
            new ParameterExpression(malformedExpression);
            fail("Should have thrown a BuilderException for the malformed expression.");
        } catch (BuilderException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    /*
     * Note for modern JUnit 5 users:
     * The same test could be written more concisely using assertThrows.
     *
     * import static org.junit.jupiter.api.Assertions.assertEquals;
     * import static org.junit.jupiter.api.Assertions.assertThrows;
     * import org.junit.jupiter.api.Test;
     *
     * @Test
     * void shouldThrowExceptionForExpressionWithUnspecifiedJdbcType_JUnit5() {
     *     String malformedExpression = "property:";
     *     String expectedErrorMessage = "Parsing error in {property:} in position 9";
     *
     *     BuilderException exception = assertThrows(BuilderException.class, () -> {
     *         new ParameterExpression(malformedExpression);
     *     });
     *
     *     assertEquals(expectedErrorMessage, exception.getMessage());
     * }
     */
}