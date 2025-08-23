package org.joda.time;

import org.junit.jupiter.api.Test;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DateTimeComparator} focusing on its handling of unsupported object types.
 */
public class DateTimeComparatorUnsupportedTypeTest {

    @Test
    public void compare_withUnsupportedObjectType_shouldThrowIllegalArgumentException() {
        // Arrange
        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());
        DateTime validDateTime = new DateTime("2000-01-01T00:00:00", DateTimeZone.UTC);
        Object unsupportedObject = "not a parsable date";

        // Act & Assert
        // The comparator should throw an exception if the left-hand side object is unsupported.
        assertThrows(IllegalArgumentException.class, () -> {
            yearComparator.compare(unsupportedObject, validDateTime);
        });

        // The comparator should also throw an exception if the right-hand side object is unsupported.
        assertThrows(IllegalArgumentException.class, () -> {
            yearComparator.compare(validDateTime, unsupportedObject);
        });
    }
}