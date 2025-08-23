package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.CallableStatement;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 * This specific test focuses on handling null inputs.
 */
// Note: The original test runner and scaffolding class are kept for context.
@org.junit.runner.RunWith(org.evosuite.runtime.EvoRunner.class)
@org.evosuite.runtime.EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true,
    useJEE = true
)
public class EnumOrdinalTypeHandler_ESTestTest6 extends EnumOrdinalTypeHandler_ESTest_scaffolding {

    /**
     * Verifies that getNullableResult throws a NullPointerException when the
     * CallableStatement provided is null.
     *
     * This is expected because the method internally tries to call a method on the
     * statement object without a prior null check.
     */
    @Test(expected = NullPointerException.class)
    public void getNullableResultFromCallableStatementShouldThrowNPEForNullStatement() throws Throwable {
        // Arrange: Create a handler for a specific enum type.
        // The enum type itself (JdbcType) is not critical, any enum would work.
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        int anyColumnIndex = 1; // The column index value doesn't affect this specific test.

        // Act & Assert: Call the method with a null statement.
        // The @Test(expected = NullPointerException.class) annotation asserts that the
        // expected exception is thrown.
        typeHandler.getNullableResult((CallableStatement) null, anyColumnIndex);
    }
}