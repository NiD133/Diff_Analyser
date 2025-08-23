package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class CustomCategoryURLGenerator_ESTest extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCloneEquality() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Vector<String> urls = new Vector<>();
        urls.add("exampleURL");
        urls.add("exampleURL");
        generator.addURLSeries(urls);
        
        Object clonedGenerator = generator.clone();
        
        assertTrue(generator.equals(clonedGenerator));
        assertNotSame(clonedGenerator, generator);
    }

    @Test(timeout = 4000)
    public void testDifferentURLSeriesEquality() throws Throwable {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        List<String> urls1 = List.of("");
        generator1.addURLSeries(urls1);
        
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        generator2.addURLSeries(null);
        
        assertFalse(generator1.equals(generator2));
        assertFalse(generator2.equals(generator1));
    }

    @Test(timeout = 4000)
    public void testDifferentURLSeriesWithMultipleEntries() throws Throwable {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        List<String> urls = List.of("url1", "url1", "url1", "url1", "", "");
        generator1.addURLSeries(urls);
        
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        assertFalse(generator1.equals(generator2));
        assertFalse(generator2.equals(generator1));
    }

    @Test(timeout = 4000)
    public void testGetURLWithInvalidIndex() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        ArrayList<String> urls = new ArrayList<>();
        generator.addURLSeries(urls);
        
        try {
            generator.getURL(0, -1052);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetURLWithOutOfBoundsIndex() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        ArrayList<String> urls = new ArrayList<>();
        generator.addURLSeries(urls);
        
        String url = generator.getURL(0, 2440);
        assertNull(url);
    }

    @Test(timeout = 4000)
    public void testAddMultipleNullURLSeries() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        ArrayList<String> urls = new ArrayList<>();
        generator.addURLSeries(urls);
        generator.addURLSeries(null);
        generator.addURLSeries(null);
        generator.addURLSeries(null);
        generator.addURLSeries(urls);
        
        String url = generator.getURL(5, -1785);
        assertNull(url);
    }

    @Test(timeout = 4000)
    public void testGetURLCount() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> urls = List.of("url1", "url1", "url1", "url1", "", "");
        generator.addURLSeries(urls);
        
        int urlCount = generator.getURLCount(0);
        assertEquals(6, urlCount);
    }

    @Test(timeout = 4000)
    public void testGetURLFromStack() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Stack<String> urls = new Stack<>();
        urls.add("");
        generator.addURLSeries(urls);
        
        String url = generator.getURL(0, 0);
        assertEquals("", url);
    }

    @Test(timeout = 4000)
    public void testGetListCount() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        ArrayList<String> urls = new ArrayList<>();
        generator.addURLSeries(urls);
        
        int listCount = generator.getListCount();
        assertEquals(1, listCount);
    }

    @Test(timeout = 4000)
    public void testGetURLCountWithInvalidIndex() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        try {
            generator.getURLCount(5570);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGenerateURLWithInvalidIndex() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        DefaultKeyedValues2DDataset<String, String> dataset = new DefaultKeyedValues2DDataset<>();
        
        try {
            generator.generateURL(dataset, -3864, -3864);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetURLFromVector() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Vector<String> urls = new Vector<>(3458);
        urls.add("anchor");
        generator.addURLSeries(urls);
        
        String url = generator.getURL(0, 0);
        assertEquals("anchor", url);
    }

    @Test(timeout = 4000)
    public void testGetURLWithNegativeIndex() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        String url = generator.getURL(5, -1785);
        assertNull(url);
    }

    @Test(timeout = 4000)
    public void testGetURLCountForEmptySeries() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        ArrayList<String> urls = new ArrayList<>();
        generator.addURLSeries(urls);
        
        int urlCount = generator.getURLCount(0);
        assertEquals(0, urlCount);
    }

    @Test(timeout = 4000)
    public void testGetURLCountForNullSeries() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(null);
        
        int urlCount = generator.getURLCount(0);
        assertEquals(0, urlCount);
    }

    @Test(timeout = 4000)
    public void testGetListCountForEmptyGenerator() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        int listCount = generator.getListCount();
        assertEquals(0, listCount);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentURLSeries() throws Throwable {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        assertTrue(generator2.equals(generator1));
        
        Vector<String> urls1 = new Vector<>();
        urls1.add("N8)j");
        generator2.addURLSeries(urls1);
        
        Stack<String> urls2 = new Stack<>();
        urls2.add(null);
        generator1.addURLSeries(urls2);
        
        assertFalse(generator1.equals(generator2));
        assertFalse(generator2.equals(generator1));
    }

    @Test(timeout = 4000)
    public void testCloneWithNullURLSeries() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Stack<String> urls = new Stack<>();
        urls.add(null);
        generator.addURLSeries(urls);
        
        Object clonedGenerator = generator.clone();
        
        assertTrue(generator.equals(clonedGenerator));
        assertNotSame(clonedGenerator, generator);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentURLSeriesTypes() throws Throwable {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        List<String> urls1 = List.of("o<]R");
        generator1.addURLSeries(urls1);
        
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        Stack<String> urls2 = new Stack<>();
        generator2.addURLSeries(urls2);
        
        assertFalse(generator2.equals(generator1));
        assertFalse(generator1.equals(generator2));
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentObjectType() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        assertFalse(generator.equals("I8p"));
    }

    @Test(timeout = 4000)
    public void testSelfEquality() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        assertTrue(generator.equals(generator));
    }

    @Test(timeout = 4000)
    public void testGetURLFromEmptyStack() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        Stack<String> urls = new Stack<>();
        generator.addURLSeries(urls);
        
        String url = generator.getURL(0, 0);
        assertNull(url);
    }

    @Test(timeout = 4000)
    public void testGetURLFromNullSeries() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(null);
        
        String url = generator.getURL(0, 0);
        assertNull(url);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentURLSeriesContent() throws Throwable {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        assertTrue(generator2.equals(generator1));
        
        Vector<String> urls1 = new Vector<>();
        urls1.add("N8)j");
        generator1.addURLSeries(urls1);
        
        Stack<String> urls2 = new Stack<>();
        urls2.add("NmY|");
        generator2.addURLSeries(urls2);
        
        assertFalse(generator1.equals(generator2));
        assertFalse(generator2.equals(generator1));
    }

    @Test(timeout = 4000)
    public void testGenerateURLWithEmptyDataset() throws Throwable {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        
        String url = generator.generateURL(dataset, 860, 0);
        assertNull(url);
    }

    @Test(timeout = 4000)
    public void testCloneEqualityWithEmptyGenerator() throws Throwable {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = (CustomCategoryURLGenerator) generator1.clone();
        
        assertTrue(generator2.equals(generator1));
        
        Vector<String> urls = new Vector<>();
        generator2.addURLSeries(urls);
        
        assertFalse(generator1.equals(generator2));
        assertFalse(generator2.equals(generator1));
    }
}