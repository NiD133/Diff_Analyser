package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test class for ConverterSet, focusing on the add() method.
 */
public class ConverterSetTest {

    // Descriptive constants for test data make the test's intent clear.
    private static final Converter BOOLEAN_CONVERTER = new TestConverter(Boolean.class);
    private static final Converter CHARACTER_CONVERTER = new TestConverter(Character.class);
    private static final Converter BYTE_CONVERTER = new TestConverter(Byte.class);
    private static final Converter SHORT_CONVERTER = new TestConverter(Short.class);

    /**
     * Tests that adding a converter that is already present in the set
     * returns the original set instance. This is an expected optimization for an
     * immutable class.
     */
    @Test
    public void add_whenConverterIsAlreadyPresent_shouldReturnSameInstance() {
        // Arrange: Create a set containing a few converters, including the one we will try to add again.
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // Act: Attempt to add a converter that is already in the set.
        // The 'removed' parameter is null because we are not interested in capturing
        // any potentially replaced converter in this test scenario.
        ConverterSet resultSet = initialSet.add(SHORT_CONVERTER, null);

        // Assert: The returned set should be the exact same instance as the original.
        // This verifies the optimization where no change results in no new object creation.
        assertSame(
            "Adding an existing converter should return the same set instance",
            initialSet,
            resultSet
        );
    }

    /**
     * Helper class to reduce boilerplate when creating simple converters for tests.
     */
    private static class TestConverter implements Converter {
        private final Class<?> supportedType;

        TestConverter(Class<?> supportedType) {
            this.supportedType = supportedType;
        }

        @Override
        public Class<?> getSupportedType() {
            return supportedType;
        }
    }
}