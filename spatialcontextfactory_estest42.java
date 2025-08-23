package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link SpatialContextFactory} class.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that the generic {@code initField} method throws an Error when attempting to
     * initialize a field of an unsupported type, such as a List.
     * <p>
     * The {@code initField} method uses reflection to set simple field types from a String map.
     * It is not designed to parse and populate complex types like the {@code readers} List,
     * which has its own dedicated initialization logic in {@code initFormats}.
     */
    @Test
    public void initField_whenFieldIsUnsupportedListType_throwsError() {
        // Arrange
        final String unsupportedFieldName = "readers";
        Map<String, String> configArgs = new HashMap<>();
        // The value is irrelevant; the test triggers on the field's type (List).
        configArgs.put(unsupportedFieldName, "some.ReaderClass");

        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = configArgs;

        // Act & Assert
        try {
            factory.initField(unsupportedFieldName);
            fail("Expected an Error because 'initField' does not support List types.");
        } catch (Error e) {
            // This is the expected outcome.
            String expectedMessage = "unsupported field type: interface java.util.List";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}