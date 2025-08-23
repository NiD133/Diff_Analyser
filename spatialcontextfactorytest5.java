package org.locationtech.spatial4j.context;

import org.junit.After;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;

/**
 * Tests the ability of {@link SpatialContextFactory} to load a custom factory implementation
 * specified via a Java system property.
 */
public class SpatialContextFactorySystemPropertyTest {

    /** The name of the system property used by SpatialContextFactory to find a factory class. */
    private static final String SPATIAL_CONTEXT_FACTORY_SYSPROP = "SpatialContextFactory";

    /**
     * A custom factory for testing that always creates a non-geographic (geo=false) SpatialContext.
     * This allows the test to verify that this specific factory was used.
     */
    public static class NonGeoSpatialContextFactory extends SpatialContextFactory {
        @Override
        public SpatialContext newSpatialContext() {
            this.geo = false; // Force the context to be non-geographic
            return new SpatialContext(this);
        }
    }

    /**
     * Ensures the system property is cleared after each test to avoid side effects.
     */
    @After
    public void tearDown() {
        System.getProperties().remove(SPATIAL_CONTEXT_FACTORY_SYSPROP);
    }

    @Test
    public void makeSpatialContext_usesFactoryFromSystemProperty_whenNotInArgs() {
        // Arrange: Set a system property to specify our custom SpatialContextFactory.
        // This factory is designed to always produce a non-geographic context.
        System.setProperty(SPATIAL_CONTEXT_FACTORY_SYSPROP, NonGeoSpatialContextFactory.class.getName());

        // Act: Create a SpatialContext without specifying a factory in the arguments map.
        // The factory should be loaded based on the system property as a fallback.
        SpatialContext ctx = SpatialContextFactory.makeSpatialContext(
                Collections.emptyMap(), getClass().getClassLoader());

        // Assert: The created context should be non-geographic, confirming that our
        // custom factory was loaded and used.
        assertFalse("Expected a non-geographic context from the custom factory", ctx.isGeo());
    }
}