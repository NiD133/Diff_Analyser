package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

// The class is renamed to be more concise and conventional.
public class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

    // Define the enum used for testing directly within the test class.
    // This makes the test self-contained and clearly shows the relationship
    // between the enum constants and their integer ordinals.
    private enum MyEnum {
        ONE, // ordinal = 0
        TWO  // ordinal = 1
    }

    private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

    /**
     * This test is part of a suite likely defined in the BaseTypeHandlerTest.
     * It verifies that the handler can correctly deserialize an enum from a
     * ResultSet by column index, using the enum's ordinal value.
     * This specific case checks that the ordinal 0 maps to MyEnum.ONE.
     */
    @Override
    @Test
    public void shouldGetResultFromResultSetByPosition() throws Exception {
        // Arrange: Set up the test conditions and expected values.
        final int enumOrdinalFromDb = 0;
        final MyEnum expectedEnum = MyEnum.ONE;

        // Mock the ResultSet to simulate returning the ordinal from the database.
        when(rs.getInt(1)).thenReturn(enumOrdinalFromDb);
        // also mock that the value was not a database NULL
        when(rs.wasNull()).thenReturn(false);

        // Act: Call the method under test.
        MyEnum actualEnum = TYPE_HANDLER.getResult(rs, 1);

        // Assert: Verify that the outcome is as expected.
        // The handler should have correctly converted the ordinal 0 into the MyEnum.ONE constant.
        assertEquals(expectedEnum, actualEnum);
    }
}