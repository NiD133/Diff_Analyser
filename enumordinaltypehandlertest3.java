package org.apache.ibatis.type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 * This test class focuses on how the handler retrieves an enum value from a ResultSet.
 */
@ExtendWith(MockitoExtension.class)
class EnumOrdinalTypeHandlerTest {

    /**
     * The enum used for testing. Defining it here makes the test self-contained
     * and the relationship between constants and their ordinals (0, 1, 2) explicit.
     */
    private enum TestEnum {
        ONE, TWO, THREE
    }

    @Mock
    private ResultSet resultSet;

    private EnumOrdinalTypeHandler<TestEnum> typeHandler;

    @BeforeEach
    void setup() {
        typeHandler = new EnumOrdinalTypeHandler<>(TestEnum.class);
    }

    @Test
    @DisplayName("Should return the correct enum constant when a valid ordinal is retrieved by column name")
    void shouldReturnEnumForValidOrdinalByColumnName() throws SQLException {
        // Arrange
        // The handler expects to read an integer (the enum's ordinal) from the database.
        final String columnName = "enum_column";
        final int enumOrdinal = 0; // The ordinal for TestEnum.ONE
        final TestEnum expectedEnum = TestEnum.ONE;

        // Mock the ResultSet to simulate reading the ordinal from the specified column.
        when(resultSet.getInt(columnName)).thenReturn(enumOrdinal);
        when(resultSet.wasNull()).thenReturn(false);

        // Act
        // The TypeHandler converts the ordinal back to its corresponding enum constant.
        TestEnum actualEnum = typeHandler.getResult(resultSet, columnName);

        // Assert
        assertEquals(expectedEnum, actualEnum);
    }

    @Test
    @DisplayName("Should return null when the database value is SQL NULL")
    void shouldReturnNullForSqlNullValue() throws SQLException {
        // Arrange
        final String columnName = "enum_column";

        // Simulate reading a value that was SQL NULL.
        // The return value of getInt() is irrelevant when wasNull() is true.
        when(resultSet.getInt(columnName)).thenReturn(0);
        when(resultSet.wasNull()).thenReturn(true);

        // Act
        TestEnum actualEnum = typeHandler.getResult(resultSet, columnName);

        // Assert
        assertNull(actualEnum, "The handler should return null for a SQL NULL value.");
    }
}