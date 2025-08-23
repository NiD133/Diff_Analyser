package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.DupDetector;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for JsonWriteContext behavior.
 *
 * Goals:
 * - Verify typical state transitions for ROOT/ARRAY/OBJECT contexts
 * - Avoid fragile assertions on internal fields
 * - Prefer public API and documented status codes
 * - Use descriptive names and arrange-act-assert structure
 */
public class JsonWriteContextTest {

    // ---------------------------------------------------------------------
    // Root context
    // ---------------------------------------------------------------------

    @Test
    public void root_initialState_isEmptyAndInRoot() {
        JsonWriteContext root = JsonWriteContext.createRootContext();

        assertTrue(root.inRoot());
        assertEquals("ROOT", root.getTypeDesc());
        assertEquals(0, root.getNestingDepth());
        assertEquals(0, root.getEntryCount());
        assertFalse(root.hasCurrentName());
        assertNull(root.getParent());
    }

    @Test
    public void root_writeMultipleValues_incrementsEntryCount() {
        JsonWriteContext root = JsonWriteContext.createRootContext();

        root.writeValue();
        root.writeValue();

        assertEquals(2, root.getEntryCount());
        // currentIndex is 0-based
        assertEquals(1, root.getCurrentIndex());
        assertTrue(root.hasCurrentIndex());
    }

    @Test
    public void withDupDetector_onRoot_attachesDetector() {
        DupDetector dd = DupDetector.rootDetector((JsonParser) null);
        JsonWriteContext root = JsonWriteContext.createRootContext().withDupDetector(dd);

        assertSame(dd, root.getDupDetector());
        assertEquals("ROOT", root.getTypeDesc());
        assertTrue(root.inRoot());
    }

    // ---------------------------------------------------------------------
    // Array context
    // ---------------------------------------------------------------------

    @Test
    public void array_createAndWriteValues_updatesIndexAndStatus() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        JsonWriteContext array = root.createChildArrayContext();

        // first array value: OK as-is, index becomes 0
        int s1 = array.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, s1);
        assertEquals(0, array.getCurrentIndex());
        assertTrue(array.inArray());

        // second array value: OK after comma, index becomes 1
        int s2 = array.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, s2);
        assertEquals(1, array.getCurrentIndex());
    }

    @Test
    public void array_hasCurrentIndex_afterFirstValueOnly() {
        JsonWriteContext array = JsonWriteContext.createRootContext().createChildArrayContext();

        assertFalse(array.hasCurrentIndex());
        array.writeValue();
        assertTrue(array.hasCurrentIndex());
        assertEquals(0, array.getCurrentIndex());
    }

    @Test
    public void array_clearAndGetParent_returnsObjectParent() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        JsonWriteContext obj = root.createChildObjectContext();
        JsonWriteContext array = obj.createChildArrayContext();

        JsonWriteContext back = array.clearAndGetParent();

        assertSame(obj, back);
        assertTrue(back.inObject());
        assertEquals("OBJECT", back.getTypeDesc());
        assertEquals(1, back.getNestingDepth());
    }

    @Test
    public void array_createdWithCurrentValue_keepsValue() {
        Object marker = new Object();
        JsonWriteContext root = JsonWriteContext.createRootContext();
        JsonWriteContext array = root.createChildArrayContext(marker);

        assertSame(marker, array.getCurrentValue());
        assertTrue(array.inArray());
    }

    // ---------------------------------------------------------------------
    // Object context
    // ---------------------------------------------------------------------

    @Test
    public void object_requiresNameBeforeValue() {
        JsonWriteContext obj = JsonWriteContext.createRootContext().createChildObjectContext();

        int status = obj.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, status);
        assertTrue(obj.inObject());
    }

    @Test
    public void object_fieldNameThenValue_transitionsAndCounts() {
        JsonWriteContext obj = JsonWriteContext.createRootContext().createChildObjectContext();

        int sName = obj.writeFieldName("a");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, sName);
        assertTrue(obj.hasCurrentName());
        assertEquals("a", obj.getCurrentName());

        int sValue = obj.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, sValue);
        assertEquals(1, obj.getEntryCount());
        // current name is still the last written field name
        assertEquals("a", obj.getCurrentName());
    }

    @Test
    public void object_multipleFields_eachNameReturnsExpectValue() {
        JsonWriteContext obj = JsonWriteContext.createRootContext().createChildObjectContext();

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, obj.writeFieldName("first"));
        obj.writeValue();

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, obj.writeFieldName("second"));
        obj.writeValue();

        assertEquals(2, obj.getEntryCount());
    }

    @Test(expected = IOException.class)
    public void object_duplicateFieldName_throwsWhenDupDetectorEnabled() throws IOException {
        DupDetector dd = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext root = JsonWriteContext.createRootContext(dd);
        JsonWriteContext obj = root.createChildObjectContext();

        obj.writeFieldName("id");
        obj.writeValue();
        // Duplicate name with duplicate detection enabled should fail
        obj.writeFieldName("id");
    }

    @Test
    public void object_clearAndGetParent_returnsRoot() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        JsonWriteContext obj = root.createChildObjectContext();

        JsonWriteContext back = obj.clearAndGetParent();

        assertSame(root, back);
        assertTrue(back.inRoot());
        assertEquals("ROOT", back.getTypeDesc());
    }

    // ---------------------------------------------------------------------
    // Type descriptors and parent links
    // ---------------------------------------------------------------------

    @Test
    public void typeDescriptions_matchContextKinds() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        JsonWriteContext array = root.createChildArrayContext();
        JsonWriteContext obj = root.createChildObjectContext();

        assertEquals("ROOT", root.getTypeDesc());
        assertEquals("ARRAY", array.getTypeDesc());
        assertEquals("OBJECT", obj.getTypeDesc());

        assertNull(root.getParent());
        assertSame(root, array.getParent());
        assertSame(root, obj.getParent());
    }
}