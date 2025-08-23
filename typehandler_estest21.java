package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link TypeHandler} class.
 */
public class TypeHandlerTest {

    /**
     * Tests that the createObject method successfully instantiates a class
     * that has a public default constructor, given its fully qualified name.
     *
     * @throws ParseException if the class cannot be found or instantiated.
     */
    @Test
    public void createObjectShouldReturnNewInstanceForExistingClass() throws ParseException {
        // Arrange: Define the fully qualified name of a class with a default constructor.
        final String className = "org.apache.commons.cli.TypeHandler";

        // Act: Call the static method to create an instance of the class.
        final Object createdObject = TypeHandler.createObject(className);

        // Assert: Verify that the returned object is not null and is of the correct type.
        assertNotNull("The created object should not be null.", createdObject);
        assertTrue("The created object should be an instance of TypeHandler.",
                   createdObject instanceof TypeHandler);
    }
}