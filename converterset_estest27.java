package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that adding a converter that is already present in the set
     * returns the original set instance, confirming its immutable behavior.
     */
    @Test
    public void add_whenConverterAlreadyExists_returnsSameInstance() {
        // Arrange
        // Create a converter that will be present in the initial set.
        Converter existingConverter = ReadableInstantConverter.INSTANCE;
        Converter[] initialConverters = new Converter[]{existingConverter};
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // Act
        // Attempt to add the exact same converter instance to the set.
        ConverterSet resultSet = initialSet.add(existingConverter, null);

        // Assert
        // The method should return the original set instance, not a new one,
        // because no change was made.
        assertSame(
            "Adding an existing converter should return the same set instance",
            initialSet,
            resultSet
        );
    }
}