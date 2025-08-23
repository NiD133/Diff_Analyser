package org.apache.ibatis.type;

import org.junit.Test;
import java.sql.ResultSet;

/**
 * Test suite for {@link ArrayTypeHandler}.
 * This test focuses on handling invalid or null arguments.
 */
public class ArrayTypeHandlerTest {

    private final ArrayTypeHandler handler = new ArrayTypeHandler();

    /**
     * Verifies that getNullableResult(ResultSet, int) throws a NullPointerException
     * when the provided ResultSet is null. This is the expected behavior as the
     * handler will attempt to dereference the null object.
     */
    @Test(expected = NullPointerException.class)
    public void getResultByIndexShouldThrowNPEForNullResultSet() {
        // Arrange: A null ResultSet is the condition under test.
        ResultSet nullResultSet = null;
        int anyColumnIndex = 1;

        // Act & Assert: Calling the method with a null ResultSet should throw a NullPointerException.
        handler.getNullableResult(nullResultSet, anyColumnIndex);
    }
}