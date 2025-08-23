package org.locationtech.spatial4j.context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.locationtech.spatial4j.shape.ShapeFactory;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that newSpatialContext() throws a RuntimeException when the configured
     * shapeFactoryClass is an interface, as interfaces cannot be instantiated.
     */
    @Test
    public void newSpatialContext_shouldThrowException_whenShapeFactoryIsAnInterface() {
        // Arrange: Create a factory and configure it with an interface instead of a
        // concrete class for the ShapeFactory.
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.shapeFactoryClass = ShapeFactory.class;

        // Assert: Expect a RuntimeException with a message indicating that the interface
        // cannot be instantiated because it lacks a suitable constructor.
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(containsString("interface org.locationtech.spatial4j.shape.ShapeFactory"));
        thrown.expectMessage(containsString("needs a constructor"));

        // Act: Attempt to create the SpatialContext, which should trigger the exception.
        factory.newSpatialContext();
    }
}