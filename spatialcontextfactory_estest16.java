package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.lang.reflect.InvocationTargetException;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that calling makeShapeFactory with a null SpatialContext
     * results in a RuntimeException caused by a NullPointerException.
     * This happens because the underlying ShapeFactory implementation
     * (ShapeFactoryImpl) attempts to dereference the null context during its construction.
     */
    @Test
    public void makeShapeFactory_whenContextIsNull_throwsRuntimeException() {
        // Arrange: Create an instance of the factory to be tested.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act & Assert: Expect a RuntimeException when the method is called with a null argument.
        try {
            factory.makeShapeFactory(null);
            fail("Expected a RuntimeException to be thrown for a null context.");
        } catch (RuntimeException e) {
            // Assert on the exception chain to ensure the failure is for the correct reason.
            // The factory's reflection-based instantiation wraps the constructor's
            // exception in an InvocationTargetException, which is then wrapped in a RuntimeException.
            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue("The cause should be an InvocationTargetException.",
                    cause instanceof InvocationTargetException);

            Throwable rootCause = cause.getCause();
            assertNotNull("The InvocationTargetException should have a cause.", rootCause);
            assertTrue("The root cause should be a NullPointerException.",
                    rootCause instanceof NullPointerException);
        }
    }
}