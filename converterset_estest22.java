package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    @Test
    public void remove_withNullConverter_shouldReturnSameInstance() {
        // Arrange: Create an empty ConverterSet.
        Converter[] emptyConverters = new Converter[0];
        ConverterSet initialSet = new ConverterSet(emptyConverters);

        // Act: Attempt to remove a null converter. Since a null converter cannot be
        // in the set, this operation should result in no change.
        ConverterSet resultSet = initialSet.remove(null, null);

        // Assert: The original set instance should be returned, confirming the
        // set was not modified.
        assertSame("Removing a null converter from a set should not change it", initialSet, resultSet);
    }
}