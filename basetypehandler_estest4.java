package org.apache.ibatis.type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Test;

/**
 * This test class contains tests for BaseTypeHandler and its concrete implementations.
 * Note: The original test class name `BaseTypeHandler_ESTestTest4` and its scaffolding
 * suggest it was auto-generated. This refactored version focuses on clarity and maintainability,
 * using EnumOrdinalTypeHandler as a concrete implementation of the abstract BaseTypeHandler.
 */
public class BaseTypeHandler_ESTestTest4 extends BaseTypeHandler_ESTest_scaffolding {

    /**
     * Tests that EnumOrdinalTypeHandler correctly sets a non-null enum parameter
     * on a PreparedStatement using the enum's ordinal value.
     */
    @Test
    public void shouldSetEnumByOrdinalOnPreparedStatement() throws SQLException {
        // Arrange
        // The EnumOrdinalTypeHandler persists an enum by its ordinal (integer) value.
        // We'll use JdbcType.ARRAY, which is the first constant and has an ordinal of 0.
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        
        JdbcType parameterToSet = JdbcType.ARRAY;
        int parameterIndex = 1;

        // Act
        // Call the method under test. The final jdbcType argument is not used by this handler.
        typeHandler.setNonNullParameter(mockPreparedStatement, parameterIndex, parameterToSet, null);

        // Assert
        // Verify that the PreparedStatement's setInt method was called with the correct
        // index and the enum's ordinal value.
        verify(mockPreparedStatement).setInt(parameterIndex, parameterToSet.ordinal());
    }
}