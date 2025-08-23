package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 * This class verifies that the handler can correctly convert between a JDBC integer (ordinal)
 * and a Java Enum constant.
 */
public class EnumOrdinalTypeHandlerTest {

    private final EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);

    /**
     * Verifies that the handler can correctly read an integer ordinal from a CallableStatement
     * and convert it into the corresponding Enum constant.
     */
    @Test
    public void shouldGetEnumByOrdinalFromCallableStatement() throws SQLException {
        // Arrange
        // We expect the database to return the ordinal for NVARCHAR.
        JdbcType expectedEnum = JdbcType.NVARCHAR;
        int enumOrdinal = expectedEnum.ordinal();
        int columnIndex = 1;

        // Mock the JDBC CallableStatement to simulate a database call.
        CallableStatement cs = mock(CallableStatement.class);
        when(cs.getInt(columnIndex)).thenReturn(enumOrdinal);
        when(cs.wasNull()).thenReturn(false);

        // Act
        // Call the method under test to get the result.
        JdbcType actualEnum = handler.getNullableResult(cs, columnIndex);

        // Assert
        // Verify that the handler returned the correct enum constant.
        assertEquals(expectedEnum, actualEnum);
    }

    /**
     * Verifies that the handler can correctly take an Enum constant and set its
     * integer ordinal on a PreparedStatement.
     */
    @Test
    public void shouldSetEnumOrdinalOnPreparedStatement() throws SQLException {
        // Arrange
        // We want to set the NVARCHAR enum as a parameter.
        JdbcType parameter = JdbcType.NVARCHAR;
        int parameterIndex = 1;
        int expectedOrdinal = parameter.ordinal();

        // Mock the JDBC PreparedStatement to simulate a database call.
        PreparedStatement ps = mock(PreparedStatement.class);

        // Act
        // Call the method under test to set the parameter.
        handler.setNonNullParameter(ps, parameterIndex, parameter, null);

        // Assert
        // Verify that the handler called the setInt method on the PreparedStatement
        // with the correct index and the enum's ordinal value.
        verify(ps).setInt(parameterIndex, expectedOrdinal);
    }
}