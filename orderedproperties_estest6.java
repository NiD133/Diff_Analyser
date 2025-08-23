package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OrderedProperties}.
 * This test focuses on the behavior of the entrySet after loading properties from a Reader.
 */
public class OrderedProperties_ESTestTest6 extends OrderedProperties_ESTest_scaffolding {

    /**
     * Tests that the entrySet is correctly populated after loading a property from a Reader.
     */
    @Test
    public void entrySetShouldContainOneEntryAfterLoadingSingleProperty() throws IOException {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final String propertyString = "test.key=test.value";
        final Reader propertyReader = new StringReader(propertyString);

        // Act
        properties.load(propertyReader);
        final Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();

        // Assert
        // Verify that exactly one entry has been loaded.
        assertEquals(1, entrySet.size());
    }
}