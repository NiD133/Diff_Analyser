package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link ArrayTypeHandler} to verify its type resolution logic.
 */
public class ArrayTypeHandlerTest {

    @Test
    public void shouldResolveJavaUtilDateToTimestampTypeName() {
        // Arrange
        ArrayTypeHandler handler = new ArrayTypeHandler();
        // According to the standard JDBC mapping, java.util.Date should correspond to TIMESTAMP.
        String expectedTypeName = JdbcType.TIMESTAMP.name();

        // Act
        // The method under test resolves the JDBC type name for a given Java class.
        String actualTypeName = handler.resolveTypeName(java.util.Date.class);

        // Assert
        assertEquals("The type handler should map java.util.Date to the TIMESTAMP type name.",
                expectedTypeName, actualTypeName);
    }
}