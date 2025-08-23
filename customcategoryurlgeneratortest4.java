package org.jfree.chart.urls;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class, focusing on serialization.
 */
class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that a CustomCategoryURLGenerator instance can be serialized and
     * deserialized, resulting in an object that is equal to the original.
     */
    @Test
    @DisplayName("A generator is equal to its serialized-deserialized counterpart")
    void serialization_restoresEqualObject() {
        // Arrange: Create a generator with multiple URL series.
        List<String> series1Urls = List.of("URL A1", "URL A2", "URL A3");
        List<String> series2Urls = List.of("URL B1", "URL B2", "URL B3");

        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        originalGenerator.addURLSeries(series1Urls);
        originalGenerator.addURLSeries(series2Urls);

        // Act: Serialize and then deserialize the generator.
        CustomCategoryURLGenerator deserializedGenerator = TestUtils.serialised(originalGenerator);

        // Assert: The deserialized generator should be equal to the original.
        assertEquals(originalGenerator, deserializedGenerator);
    }
}