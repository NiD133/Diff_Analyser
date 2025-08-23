package org.apache.ibatis.type;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 */
public class EnumOrdinalTypeHandlerTest {

    // Rule to enable Mockito annotations and simplify mock creation.
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    // Rule for testing exceptions in a declarative way.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private CallableStatement mockCallableStatement;

    private EnumOrdinalTypeHandler<JdbcType> typeHandler;

    @Before
    public void setUp() {
        // Initialize the handler for the JdbcType enum before each test.
        typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
    }

    @Test
    public void getNullableResult_ShouldThrowIllegalArgumentException_WhenOrdinalIsInvalid() throws SQLException {
        // Arrange
        final int invalidOrdinal = -1; // An ordinal can never be negative.
        final int anyColumnIndex = 1;
        
        // Configure the mock to return an invalid ordinal from the database.
        when(mockCallableStatement.getInt(anyColumnIndex)).thenReturn(invalidOrdinal);

        // Assert: Expect an exception with a specific message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot convert -1 to JdbcType by ordinal value.");

        // Act: Attempt to get the result with the invalid ordinal.
        typeHandler.getNullableResult(mockCallableStatement, anyColumnIndex);
    }
}