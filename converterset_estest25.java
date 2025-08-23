package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that adding a new converter instance for an already-supported type
     * returns a new ConverterSet instance.
     *
     * This verifies the immutable nature of ConverterSet: modifications should
     * result in a new object rather than changing the original.
     */
    @Test
    public void add_whenReplacingConverterForExistingType_shouldReturnNewSet() {
        // Arrange
        // 1. Create an initial set containing the standard singleton converter.
        Converter initialConverter = ReadableInstantConverter.INSTANCE;
        ConverterSet initialSet = new ConverterSet(new Converter[]{initialConverter});

        // 2. Create a new, distinct instance of the same converter type.
        //    This is possible because the constructor is package-private.
        Converter replacingConverter = new ReadableInstantConverter();

        // Act
        // 3. Add the new converter. This should replace the existing one because it
        //    handles the same type, but since it's a different instance, a new
        //    set must be created.
        ConverterSet resultSet = initialSet.add(replacingConverter, null);

        // Assert
        // 4. Verify that the returned set is a different instance from the original,
        //    confirming the immutability of ConverterSet.
        assertNotSame("A new ConverterSet instance should be returned when a converter is replaced",
                initialSet, resultSet);
    }
}