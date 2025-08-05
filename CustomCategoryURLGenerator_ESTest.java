import org.jfree.chart.urls.CustomCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 *
 * The tests are organized into nested classes based on the method being tested.
 */
class CustomCategoryURLGeneratorTest {

    private CustomCategoryURLGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new CustomCategoryURLGenerator();
    }

    @Nested
    @DisplayName("URL List Management (addURLSeries, getListCount, getURLCount)")
    class UrlListManagement {

        @Test
        @DisplayName("A new generator should have zero URL lists")
        void newGeneratorShouldBeEmpty() {
            assertEquals(0, generator.getListCount());
        }

        @Test
        @DisplayName("getListCount should return the number of URL lists added")
        void getListCountShouldReflectAddedSeries() {
            generator.addURLSeries(List.of("url1"));
            assertEquals(1, generator.getListCount());

            generator.addURLSeries(List.of("url2", "url3"));
            assertEquals(2, generator.getListCount());
        }

        @Test
        @DisplayName("getURLCount should return the number of URLs in a specific list")
        void getURLCountShouldReturnUrlCountForSeries() {
            List<String> urls = List.of("url1.1", "url1.2", "url1.3");
            generator.addURLSeries(urls);
            assertEquals(3, generator.getURLCount(0));
        }

        @Test
        @DisplayName("getURLCount should return 0 for an empty URL list")
        void getURLCountShouldReturnZeroForEmptyList() {
            generator.addURLSeries(Collections.emptyList());
            assertEquals(0, generator.getURLCount(0));
        }

        @Test
        @DisplayName("getURLCount should return 0 for a null URL list")
        void getURLCountShouldReturnZeroForNullList() {
            generator.addURLSeries(null);
            assertEquals(0, generator.getURLCount(0));
        }

        @Test
        @DisplayName("getURLCount should throw IndexOutOfBoundsException for an invalid series index")
        void getURLCountShouldThrowExceptionForInvalidSeriesIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> generator.getURLCount(0));
            assertThrows(IndexOutOfBoundsException.class, () -> generator.getURLCount(-1));
        }
    }

    @Nested
    @DisplayName("URL Retrieval (getURL, generateURL)")
    class UrlRetrieval {

        @Test
        @DisplayName("should return the correct URL for valid series and item indices")
        void getURLShouldReturnCorrectUrlForValidIndices() {
            List<String> series0Urls = List.of("s0-item0", "s0-item1");
            List<String> series1Urls = List.of("s1-item0");
            generator.addURLSeries(series0Urls);
            generator.addURLSeries(series1Urls);

            assertEquals("s0-item1", generator.getURL(0, 1));
            assertEquals("s1-item0", generator.getURL(1, 0));
        }

        @Test
        @DisplayName("generateURL should return the correct URL, ignoring the dataset")
        void generateURLShouldBehaveIdenticallyToGetURL() {
            List<String> series0Urls = List.of("s0-item0", "s0-item1");
            generator.addURLSeries(series0Urls);
            DefaultCategoryDataset dataset = new DefaultCategoryDataset(); // Dataset is ignored by this implementation

            assertEquals("s0-item1", generator.generateURL(dataset, 0, 1));
        }

        @Test
        @DisplayName("should return null for an out-of-bounds series index")
        void getURLShouldReturnNullForInvalidSeriesIndex() {
            generator.addURLSeries(List.of("url"));
            assertNull(generator.getURL(1, 0), "Accessing a series index greater than size should return null");
            assertNull(generator.getURL(99, 0), "Accessing a high series index should return null");
        }

        @Test
        @DisplayName("should return null for an out-of-bounds positive item index")
        void getURLShouldReturnNullForInvalidPositiveItemIndex() {
            generator.addURLSeries(List.of("url1", "url2"));
            assertNull(generator.getURL(0, 2), "Accessing an item index equal to size should return null");
            assertNull(generator.getURL(0, 99), "Accessing a high item index should return null");
        }

        @Test
        @DisplayName("should throw IndexOutOfBoundsException for a negative item index")
        void getURLShouldThrowExceptionForNegativeItemIndex() {
            generator.addURLSeries(List.of("url1", "url2"));
            assertThrows(IndexOutOfBoundsException.class, () -> generator.getURL(0, -1));
        }

        @Test
        @DisplayName("should return null if the URL list for the series is null or empty")
        void getURLShouldReturnNullForNullOrEmptyUrlList() {
            generator.addURLSeries(null);
            generator.addURLSeries(Collections.emptyList());

            assertNull(generator.getURL(0, 0), "Accessing an item from a null list should return null");
            assertNull(generator.getURL(1, 0), "Accessing an item from an empty list should return null");
        }
    }

    @Nested
    @DisplayName("Object Methods (equals, hashCode, clone)")
    class ObjectMethods {

        @Test
        @DisplayName("equals should be true for two generators with identical URL lists")
        void equalsShouldBeTrueForIdenticalContent() {
            List<String> urls1 = List.of("a", "b");
            List<String> urls2 = List.of("c");
            generator.addURLSeries(urls1);
            generator.addURLSeries(urls2);

            CustomCategoryURLGenerator other = new CustomCategoryURLGenerator();
            other.addURLSeries(new ArrayList<>(urls1)); // Use new list to ensure it's not the same instance
            other.addURLSeries(new ArrayList<>(urls2));

            assertTrue(generator.equals(other));
            assertTrue(other.equals(generator), "equals should be symmetric");
            assertEquals(generator.hashCode(), other.hashCode(), "Hash codes should be equal for equal objects");
        }

        @Test
        @DisplayName("equals should be false for generators with different content")
        void equalsShouldBeFalseForDifferentContent() {
            generator.addURLSeries(List.of("a", "b"));

            CustomCategoryURLGenerator otherDifferentUrl = new CustomCategoryURLGenerator();
            otherDifferentUrl.addURLSeries(List.of("a", "c")); // Different URL
            assertFalse(generator.equals(otherDifferentUrl));

            CustomCategoryURLGenerator otherDifferentListCount = new CustomCategoryURLGenerator();
            otherDifferentListCount.addURLSeries(List.of("a")); // Different list size
            assertFalse(generator.equals(otherDifferentListCount));
        }

        @Test
        @DisplayName("equals should handle null and empty lists correctly")
        void equalsShouldDifferentiateNullAndEmptyLists() {
            generator.addURLSeries(null);

            CustomCategoryURLGenerator otherWithEmptyList = new CustomCategoryURLGenerator();
            otherWithEmptyList.addURLSeries(Collections.emptyList());

            assertFalse(generator.equals(otherWithEmptyList), "A null list should not be equal to an empty list");
        }

        @Test
        @DisplayName("equals should follow its general contract")
        void equalsGeneralContract() {
            assertTrue(generator.equals(generator), "An object must be equal to itself (reflexive).");
            assertFalse(generator.equals(null), "An object must not be equal to null.");
            assertFalse(generator.equals("a string"), "An object must not be equal to an object of a different type.");
        }

        @Test
        @DisplayName("clone should produce an independent object that is equal to the original")
        void cloneShouldCreateEqualAndIndependentObject() throws CloneNotSupportedException {
            // Arrange: Setup original generator with various list types
            generator.addURLSeries(List.of("url1", "url2"));
            generator.addURLSeries(null);
            generator.addURLSeries(Collections.emptyList());

            // Act: Clone the generator
            CustomCategoryURLGenerator clone = (CustomCategoryURLGenerator) generator.clone();

            // Assert: Check for equality and independence
            assertNotSame(generator, clone, "Clone should be a different instance.");
            assertEquals(generator, clone, "Clone should be equal to the original.");

            // Verify independence (deep copy) by modifying the clone
            clone.addURLSeries(List.of("new url"));
            assertNotEquals(generator, clone, "Modifying the clone should make it unequal to the original.");
            assertEquals(3, generator.getListCount(), "Original list count should remain unchanged.");
            assertEquals(4, clone.getListCount(), "Clone's list count should be updated.");
        }
    }
}