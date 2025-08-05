package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;

/**
 * Test suite for CustomCategoryURLGenerator functionality.
 * Tests URL generation, series management, equality, and cloning behavior.
 */
public class CustomCategoryURLGeneratorTest {

    // ========== Clone and Equality Tests ==========
    
    @Test
    public void testCloneWithURLSeries_ShouldCreateEqualButSeparateInstance() {
        // Given: A generator with URL series containing duplicate URLs
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Vector<String> urls = new Vector<>();
        urls.add("http://example.com/item1");
        urls.add("http://example.com/item1"); // duplicate URL
        generator.addURLSeries(urls);
        
        // When: Cloning the generator
        Object clonedGenerator = generator.clone();
        
        // Then: Clone should be equal but not the same instance
        assertTrue("Cloned generator should be equal to original", 
                  generator.equals(clonedGenerator));
        assertNotSame("Cloned generator should be a separate instance", 
                     clonedGenerator, generator);
    }
    
    @Test
    public void testEquals_WithDifferentURLSeries_ShouldReturnFalse() {
        // Given: Two generators with different URL series
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        List<String> urls = List.of("http://example.com");
        generator1.addURLSeries(urls);
        generator2.addURLSeries(null);
        
        // When & Then: Generators with different series should not be equal
        assertFalse("Generators with different URL series should not be equal", 
                   generator1.equals(generator2));
        assertFalse("Equality should be symmetric", 
                   generator2.equals(generator1));
    }
    
    @Test
    public void testEquals_WithURLSeriesVsEmpty_ShouldReturnFalse() {
        // Given: One generator with URLs, one empty
        CustomCategoryURLGenerator generatorWithUrls = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator emptyGenerator = new CustomCategoryURLGenerator();
        
        List<String> urls = List.of("http://example.com/1", "http://example.com/2", 
                                   "http://example.com/3", "http://example.com/4", 
                                   "", "");
        generatorWithUrls.addURLSeries(urls);
        
        // When & Then: Generator with URLs should not equal empty generator
        assertFalse("Generator with URLs should not equal empty generator", 
                   generatorWithUrls.equals(emptyGenerator));
        assertFalse("Equality should be symmetric", 
                   emptyGenerator.equals(generatorWithUrls));
    }

    // ========== URL Retrieval Tests ==========
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetURL_WithNegativeItemIndex_ShouldThrowException() {
        // Given: Generator with empty URL series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(new ArrayList<>());
        
        // When & Then: Accessing negative item index should throw exception
        generator.getURL(0, -1052);
    }
    
    @Test
    public void testGetURL_WithItemIndexBeyondSeriesSize_ShouldReturnNull() {
        // Given: Generator with empty URL series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(new ArrayList<>());
        
        // When: Accessing item beyond series size
        String url = generator.getURL(0, 2440);
        
        // Then: Should return null
        assertNull("URL beyond series size should return null", url);
    }
    
    @Test
    public void testGetURL_WithMultipleSeriesAndInvalidIndex_ShouldReturnNull() {
        // Given: Generator with multiple series including nulls
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(new ArrayList<>());
        generator.addURLSeries(null);
        generator.addURLSeries(null);
        generator.addURLSeries(null);
        generator.addURLSeries(new ArrayList<>());
        
        // When: Accessing invalid item index
        String url = generator.getURL(5, -1785);
        
        // Then: Should return null
        assertNull("Invalid item index should return null", url);
    }
    
    @Test
    public void testGetURL_WithValidIndex_ShouldReturnCorrectURL() {
        // Given: Generator with URL series containing empty string
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Stack<String> urls = new Stack<>();
        urls.add("");
        generator.addURLSeries(urls);
        
        // When: Accessing valid index
        String url = generator.getURL(0, 0);
        
        // Then: Should return the URL (empty string in this case)
        assertEquals("Should return the URL at specified index", "", url);
    }
    
    @Test
    public void testGetURL_WithAnchorURL_ShouldReturnAnchor() {
        // Given: Generator with anchor URL
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Vector<String> urls = new Vector<>(100);
        urls.add("anchor");
        generator.addURLSeries(urls);
        
        // When: Accessing the URL
        String url = generator.getURL(0, 0);
        
        // Then: Should return the anchor URL
        assertEquals("Should return anchor URL", "anchor", url);
    }
    
    @Test
    public void testGetURL_WithNoSeries_ShouldReturnNull() {
        // Given: Generator with no URL series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // When: Accessing URL from non-existent series
        String url = generator.getURL(5, -1785);
        
        // Then: Should return null
        assertNull("URL from non-existent series should return null", url);
    }

    // ========== URL Count Tests ==========
    
    @Test
    public void testGetURLCount_WithMultipleURLs_ShouldReturnCorrectCount() {
        // Given: Generator with 6 URLs in series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> urls = List.of("http://example.com/1", "http://example.com/2", 
                                   "http://example.com/3", "http://example.com/4", 
                                   "", "");
        generator.addURLSeries(urls);
        
        // When: Getting URL count
        int count = generator.getURLCount(0);
        
        // Then: Should return correct count
        assertEquals("Should return correct URL count", 6, count);
    }
    
    @Test
    public void testGetURLCount_WithEmptySeries_ShouldReturnZero() {
        // Given: Generator with empty URL series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(new ArrayList<>());
        
        // When: Getting URL count
        int count = generator.getURLCount(0);
        
        // Then: Should return zero
        assertEquals("Empty series should have zero URL count", 0, count);
    }
    
    @Test
    public void testGetURLCount_WithNullSeries_ShouldReturnZero() {
        // Given: Generator with null URL series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(null);
        
        // When: Getting URL count
        int count = generator.getURLCount(0);
        
        // Then: Should return zero
        assertEquals("Null series should have zero URL count", 0, count);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetURLCount_WithInvalidSeriesIndex_ShouldThrowException() {
        // Given: Generator with no series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // When & Then: Accessing invalid series index should throw exception
        generator.getURLCount(5570);
    }

    // ========== Series Count Tests ==========
    
    @Test
    public void testGetListCount_WithOneSeries_ShouldReturnOne() {
        // Given: Generator with one URL series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(new ArrayList<>());
        
        // When: Getting list count
        int count = generator.getListCount();
        
        // Then: Should return one
        assertEquals("Should return correct list count", 1, count);
    }
    
    @Test
    public void testGetListCount_WithNoSeries_ShouldReturnZero() {
        // Given: New generator with no series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // When: Getting list count
        int count = generator.getListCount();
        
        // Then: Should return zero
        assertEquals("New generator should have zero list count", 0, count);
    }

    // ========== Generate URL Tests ==========
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGenerateURL_WithInvalidIndices_ShouldThrowException() {
        // Given: Generator and dataset with invalid indices
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        DefaultKeyedValues2DDataset<String, String> dataset = new DefaultKeyedValues2DDataset<>();
        
        // When & Then: Invalid indices should throw exception
        generator.generateURL(dataset, -3864, -3864);
    }
    
    @Test
    public void testGenerateURL_WithValidDataset_ShouldReturnNull() {
        // Given: Generator with no URL series and valid dataset
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        
        // When: Generating URL
        String url = generator.generateURL(dataset, 860, 0);
        
        // Then: Should return null (no URL series configured)
        assertNull("Should return null when no URL series configured", url);
    }

    // ========== Additional Equality Tests ==========
    
    @Test
    public void testEquals_WithDifferentURLContent_ShouldReturnFalse() {
        // Given: Two generators with different URL content
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        Vector<String> urls1 = new Vector<>();
        urls1.add("http://site1.com");
        generator1.addURLSeries(urls1);
        
        Stack<String> urls2 = new Stack<>();
        urls2.add(null);
        generator2.addURLSeries(urls2);
        
        // When & Then: Generators with different URL content should not be equal
        assertFalse("Generators with different URL content should not be equal", 
                   generator1.equals(generator2));
        assertFalse("Equality should be symmetric", 
                   generator2.equals(generator1));
    }
    
    @Test
    public void testEquals_WithNullURLs_ShouldHandleCorrectly() {
        // Given: Generator with null URLs and its clone
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Stack<String> urls = new Stack<>();
        urls.add(null);
        generator.addURLSeries(urls);
        
        // When: Cloning and comparing
        Object clonedGenerator = generator.clone();
        boolean isEqual = generator.equals(clonedGenerator);
        
        // Then: Should be equal despite null URLs
        assertNotSame("Clone should be separate instance", clonedGenerator, generator);
        assertTrue("Generator with null URLs should equal its clone", isEqual);
    }
    
    @Test
    public void testEquals_WithDifferentSeriesSizes_ShouldReturnFalse() {
        // Given: Two generators with different series sizes
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        List<String> urls = List.of("http://example.com");
        generator1.addURLSeries(urls);
        
        Stack<String> emptyUrls = new Stack<>();
        generator2.addURLSeries(emptyUrls);
        
        // When & Then: Different series sizes should not be equal
        assertFalse("Different series sizes should not be equal", 
                   generator2.equals(generator1));
        assertFalse("Equality should be symmetric", 
                   generator1.equals(generator2));
    }
    
    @Test
    public void testEquals_WithNonCustomCategoryURLGenerator_ShouldReturnFalse() {
        // Given: Generator and string object
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        String notAGenerator = "not a generator";
        
        // When & Then: Should not be equal to different type
        assertFalse("Should not equal different object type", 
                   generator.equals(notAGenerator));
    }
    
    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        // Given: Single generator instance
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // When & Then: Should equal itself
        assertTrue("Generator should equal itself", generator.equals(generator));
    }

    // ========== Edge Cases ==========
    
    @Test
    public void testGetURL_WithEmptyStackSeries_ShouldReturnNull() {
        // Given: Generator with empty stack series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Stack<String> emptyStack = new Stack<>();
        generator.addURLSeries(emptyStack);
        
        // When: Accessing URL from empty stack
        String url = generator.getURL(0, 0);
        
        // Then: Should return null
        assertNull("Empty stack series should return null", url);
    }
    
    @Test
    public void testGetURL_WithNullSeries_ShouldReturnNull() {
        // Given: Generator with null series
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(null);
        
        // When: Accessing URL from null series
        String url = generator.getURL(0, 0);
        
        // Then: Should return null
        assertNull("Null series should return null", url);
    }
    
    @Test
    public void testEquals_WithDifferentURLValues_ShouldReturnFalse() {
        // Given: Two generators with different URL values
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        Vector<String> urls1 = new Vector<>();
        urls1.add("http://site1.com");
        generator1.addURLSeries(urls1);
        
        Stack<String> urls2 = new Stack<>();
        urls2.add("http://site2.com");
        generator2.addURLSeries(urls2);
        
        // When & Then: Different URL values should not be equal
        assertFalse("Different URL values should not be equal", 
                   generator1.equals(generator2));
        assertFalse("Equality should be symmetric", 
                   generator2.equals(generator1));
    }
    
    @Test
    public void testClone_WithEmptyGenerator_ShouldCreateEqualInstance() {
        // Given: Empty generator
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // When: Cloning empty generator
        CustomCategoryURLGenerator clonedGenerator = (CustomCategoryURLGenerator) generator.clone();
        
        // Then: Should be equal initially
        assertTrue("Empty generators should be equal initially", 
                  clonedGenerator.equals(generator));
        
        // When: Adding series to clone
        Vector<String> urls = new Vector<>();
        clonedGenerator.addURLSeries(urls);
        
        // Then: Should no longer be equal
        assertFalse("Modified clone should not equal original", 
                   generator.equals(clonedGenerator));
        assertFalse("Equality should be symmetric", 
                   clonedGenerator.equals(generator));
    }
}