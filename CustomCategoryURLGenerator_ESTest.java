package org.jfree.chart.urls;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for CustomCategoryURLGenerator.
 * These tests cover:
 * - Series/list bookkeeping (list count and per-list URL count)
 * - URL lookup behavior (in-range, out-of-range, null series)
 * - Equality and type-safety
 * - Clone behavior (including deep copy of internal lists)
 * - generateURL delegating to the same semantics as getURL
 */
public class CustomCategoryURLGeneratorTest {

    // -----------------------------
    // List/series bookkeeping
    // -----------------------------

    @Test
    public void listCountReflectsAddedSeries() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();

        assertEquals("No lists by default", 0, gen.getListCount());

        gen.addURLSeries(null); // null series is allowed and counts as a list
        assertEquals(1, gen.getListCount());

        gen.addURLSeries(Collections.emptyList());
        assertEquals(2, gen.getListCount());
    }

    @Test
    public void urlCountReturnsSizeOrZeroForNullSeries() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();

        gen.addURLSeries(Arrays.asList("u1"));
        gen.addURLSeries(null);

        assertEquals(1, gen.getURLCount(0));
        assertEquals(0, gen.getURLCount(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void urlCountThrowsForInvalidListIndex() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        gen.getURLCount(123); // no lists yet
    }

    // -----------------------------
    // URL retrieval semantics
    // -----------------------------

    @Test
    public void getURLReturnsValueWhenPresent() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        gen.addURLSeries(Arrays.asList("first", "second"));

        assertEquals("first", gen.getURL(0, 0));
        assertEquals("second", gen.getURL(0, 1));
    }

    @Test
    public void getURLReturnsNullWhenItemOutOfRange() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        gen.addURLSeries(Collections.emptyList());

        assertNull(gen.getURL(0, 999)); // list exists but has no items
    }

    @Test
    public void getURLReturnsNullWhenSeriesOutOfRange() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();

        assertNull(gen.getURL(5, 0)); // no lists at all
    }

    @Test
    public void getURLReturnsNullForNullSeries() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        gen.addURLSeries(null);

        assertNull(gen.getURL(0, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getURLThrowsForNegativeItemIndexOnExistingSeries() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        gen.addURLSeries(Collections.emptyList());

        gen.getURL(0, -1);
    }

    // -----------------------------
    // generateURL mirrors getURL
    // -----------------------------

    @Test
    public void generateURLReturnsSameAsGetURL() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        gen.addURLSeries(Arrays.asList("link"));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        assertEquals("link", gen.generateURL(dataset, 0, 0));
        assertNull(gen.generateURL(dataset, 0, 999));
        assertNull(gen.generateURL(dataset, 5, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void generateURLThrowsForNegativeIndexes() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        gen.generateURL(dataset, -1, -1);
    }

    // -----------------------------
    // Equality and type safety
    // -----------------------------

    @Test
    public void equalsIsReflexiveAndTypeSafe() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();

        assertTrue(gen.equals(gen));
        assertFalse(gen.equals("not a generator"));
        assertFalse(gen.equals(null));
    }

    @Test
    public void equalsConsidersSeriesContent() {
        CustomCategoryURLGenerator a = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator b = new CustomCategoryURLGenerator();

        assertTrue(a.equals(b)); // both empty

        a.addURLSeries(Arrays.asList("a"));
        b.addURLSeries(Arrays.asList("a"));
        assertTrue(a.equals(b)); // same content

        // different sizes
        a.addURLSeries(Collections.singletonList("extra"));
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));

        // now match sizes but with different values
        b.addURLSeries(Collections.singletonList("different"));
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equalsHandlesNullEntriesInsideSeries() {
        CustomCategoryURLGenerator a = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator b = new CustomCategoryURLGenerator();

        a.addURLSeries(Collections.singletonList(null));
        b.addURLSeries(Collections.singletonList(null));

        assertTrue(a.equals(b));
    }

    @Test
    public void nullAndEmptySeriesAreNotEqual() {
        CustomCategoryURLGenerator a = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator b = new CustomCategoryURLGenerator();

        a.addURLSeries(Collections.emptyList());
        b.addURLSeries(null);

        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    // -----------------------------
    // Cloning
    // -----------------------------

    @Test
    public void cloneProducesEqualButIndependentCopy() throws CloneNotSupportedException {
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(Arrays.asList("one"));

        CustomCategoryURLGenerator copy = (CustomCategoryURLGenerator) original.clone();
        assertNotSame(original, copy);
        assertTrue(original.equals(copy));

        // Mutate the clone's state; originals should remain equal only if state is identical
        copy.addURLSeries(Collections.singletonList("new-series"));
        assertFalse(original.equals(copy));
        assertFalse(copy.equals(original));
    }

    @Test
    public void cloneIsDeepCopyOfSeriesLists() throws CloneNotSupportedException {
        // Use a mutable list so we can mutate after cloning
        ArrayList<String> mutableSeries = new ArrayList<>(Collections.singletonList("initial"));

        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(mutableSeries);

        CustomCategoryURLGenerator snapshot = (CustomCategoryURLGenerator) original.clone();
        assertTrue(original.equals(snapshot));

        // Mutate the original list instance after cloning
        mutableSeries.add("mutation-after-clone");

        // If clone performed a deep copy of the series lists, it should now differ from original
        assertFalse("Clone should be independent of external list mutations", original.equals(snapshot));
        assertFalse(snapshot.equals(original));
    }
}