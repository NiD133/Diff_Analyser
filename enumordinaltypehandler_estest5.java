package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.ResultSet;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 */
class EnumOrdinalTypeHandlerTest {

    /**
     * Verifies that getNullableResult(ResultSet, int) throws a NullPointerException
     * when the provided ResultSet is null. The underlying database driver would typically
     * throw this exception when methods are called on a null object.
     */
    @Test
    void shouldThrowNullPointerExceptionWhenResultSetIsNullForGetResultByIndex() {
        // Arrange: Create a handler for a standard enum type.
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        int anyColumnIndex = 1;

        // Act & Assert: Expect a NullPointerException when the method is called with a null ResultSet.
        assertThrows(NullPointerException.class, () -> {
            typeHandler.getNullableResult((ResultSet) null, anyColumnIndex);
        });
    }
}