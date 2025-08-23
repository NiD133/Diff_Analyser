package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 */
// The class was renamed from ArrayTypeHandlerTestTest10 to improve clarity and remove redundancy.
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

    /**
     * This test verifies that the handler correctly returns null when a database
     * procedure call (via CallableStatement) returns a null SQL ARRAY.
     * The method name is defined by the abstract base class.
     */
    @Override
    @Test
    void shouldGetResultNullFromCallableStatement() throws Exception {
        // Arrange: Configure the mock CallableStatement to return null.
        // The 'cs' mock is provided by the BaseTypeHandlerTest base class.
        when(cs.getArray(1)).thenReturn(null);

        // Act: Execute the method under test.
        Object result = TYPE_HANDLER.getResult(cs, 1);

        // Assert: Verify that the handler correctly interpreted the null array as a null result.
        assertNull(result, "The type handler should return null when the database value is null.");
    }
}