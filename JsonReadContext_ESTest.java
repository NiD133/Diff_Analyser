package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for JsonReadContext behavior.
 * These tests avoid internal fields and EvoSuite-specific constructs,
 * and exercise the public API in small, single-purpose scenarios.
 */
public class JsonReadContextTest {

    // Helper to create a root context without duplicate detection
    private static JsonReadContext newRoot() {
        return JsonReadContext.createRootContext((DupDetector) null);
    }

    // Helper to create a root context with explicit coordinates
    private static JsonReadContext newRootAt(int line, int col) {
        return JsonReadContext.createRootContext(line, col, null);
    }

    @Test
    public void root_hasExpectedDefaults() {
        JsonReadContext root = newRoot();

        assertTrue(root.inRoot());
        assertFalse(root.inArray());
        assertFalse(root.inObject());

        assertEquals("ROOT", root.getTypeDesc());
        assertEquals(0, root.getNestingDepth());
        assertEquals(-1, root.getCurrentIndex());
        assertFalse(root.hasCurrentName());
        assertNull(root.getParent());
    }

    @Test
    public void childObject_increasesDepth_andHasParent() {
        JsonReadContext root = newRoot();
        JsonReadContext obj = root.createChildObjectContext(1, 1);

        assertTrue(obj.inObject());
        assertFalse(obj.inArray());
        assertFalse(obj.inRoot());

        assertEquals("OBJECT", obj.getTypeDesc());
        assertEquals(1, obj.getNestingDepth());
        assertSame(root, obj.getParent());
    }

    @Test
    public void childArray_increasesDepth_andIsArray() {
        JsonReadContext root = newRoot();
        JsonReadContext arr = root.createChildArrayContext(2, 3);

        assertTrue(arr.inArray());
        assertFalse(arr.inObject());
        assertFalse(arr.inRoot());

        assertEquals("ARRAY", arr.getTypeDesc());
        assertEquals(1, arr.getNestingDepth());
        assertSame(root, arr.getParent());
    }

    @Test
    public void child_instances_areReusedByType() {
        JsonReadContext root = newRoot();

        JsonReadContext firstObj = root.createChildObjectContext(10, 10);
        JsonReadContext secondObj = root.createChildObjectContext(20, 20);
        assertSame("Object context should be reused", firstObj, secondObj);

        JsonReadContext firstArr = root.createChildArrayContext(1, 1);
        JsonReadContext secondArr = root.createChildArrayContext(2, 2);
        assertSame("Array context should be reused", firstArr, secondArr);

        // Object and Array contexts are distinct
        assertNotSame(firstObj, firstArr);
    }

    @Test
    public void expectComma_tracksEntryIndex() {
        JsonReadContext arr = newRoot().createChildArrayContext(1, 1);

        // First element: index moves to 0, but no comma expected
        assertFalse(arr.expectComma());
        assertEquals(0, arr.getCurrentIndex());

        // Second element: index moves to 1, comma is expected
        assertTrue(arr.expectComma());
        assertEquals(1, arr.getCurrentIndex());

        // Third element: index moves to 2, comma is expected
        assertTrue(arr.expectComma());
        assertEquals(2, arr.getCurrentIndex());
    }

    @Test
    public void startLocation_usesProvidedLineAndColumn() {
        JsonReadContext root = newRootAt(11, 7);
        ContentReference src = ContentReference.unknown();

        JsonLocation loc = root.startLocation(src);
        assertEquals(11, loc.getLineNr());
        assertEquals(7, loc.getColumnNr());
    }

    @Test
    public void startLocation_defaultsForNoCoords() {
        JsonReadContext root = newRoot();
        ContentReference src = ContentReference.unknown();

        JsonLocation loc = root.startLocation(src);
        // Default root location is line 1, column 0 in Jackson
        assertEquals(1, loc.getLineNr());
        assertEquals(0, loc.getColumnNr());
    }

    @Test
    public void currentName_setGetAndHas() throws Exception {
        JsonReadContext obj = newRoot().createChildObjectContext(1, 1);

        assertFalse(obj.hasCurrentName());
        assertNull(obj.getCurrentName());

        obj.setCurrentName("id");
        assertTrue(obj.hasCurrentName());
        assertEquals("id", obj.getCurrentName());
    }

    @Test
    public void duplicateFieldName_throwsWithDupDetector() throws Exception {
        // Create a parser just to wire up the DupDetector (location details are not asserted)
        JsonFactory f = new JsonFactory();
        JsonParser p = f.createParser("{}");

        DupDetector dd = DupDetector.rootDetector(p);
        JsonReadContext ctx = JsonReadContext.createRootContext(dd);

        // First set of the same name is fine
        ctx.setCurrentName("name");

        // Second set should be reported as a duplicate
        Exception e = assertThrows(Exception.class, () -> ctx.setCurrentName("name"));
        // Jackson throws a JsonProcessingException (JsonParseException); we avoid asserting the exact type/message
        assertNotNull(e.getMessage());
    }

    @Test
    public void clearAndGetParent_returnsParent_andClearsCurrentValue() {
        JsonReadContext root = newRoot();
        JsonReadContext child = root.createChildObjectContext(1, 1);

        child.setCurrentValue("marker");
        assertEquals("marker", child.getCurrentValue());

        JsonReadContext backToParent = child.clearAndGetParent();
        assertSame(root, backToParent);

        // After clearing, the child should not retain current value
        assertNull(child.getCurrentValue());
    }

    @Test
    public void parentOfRoot_isNull() {
        assertNull(newRoot().getParent());
    }

    @Test
    public void getAndSetCurrentValue_roundTrip() {
        JsonReadContext root = newRoot();
        assertNull(root.getCurrentValue());

        Object value = new Object();
        root.setCurrentValue(value);
        assertSame(value, root.getCurrentValue());
    }

    @Test
    public void withDupDetector_enablesDuplicateChecks() throws Exception {
        JsonReadContext root = newRoot();
        JsonReadContext obj = root.createChildObjectContext(1, 1);

        // Initially no dup checks; setting same name twice is allowed
        obj.setCurrentName("a");
        obj.setCurrentName("a");

        // Enable dup checks and verify duplicate is reported
        JsonFactory f = new JsonFactory();
        JsonParser p = f.createParser("{}");
        DupDetector dd = DupDetector.rootDetector(p);

        obj.withDupDetector(dd);
        obj.setCurrentName("b"); // first time ok

        Exception e = assertThrows(Exception.class, () -> obj.setCurrentName("b"));
        assertNotNull(e.getMessage());
    }
}