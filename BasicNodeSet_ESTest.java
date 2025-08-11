package org.apache.commons.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for BasicNodeSet.
 *
 * These tests aim to verify BasicNodeSet’s public contract without relying on
 * implementation details (such as specific exception types/messages thrown by
 * internal pipelines or variable resolution).
 */
public class BasicNodeSetTest {

    // Helpers

    private static VariablePointer newVarPointer(String name) {
        return new VariablePointer(new BasicVariables(), new QName(name));
    }

    // Empty set behavior

    @Test
    public void emptyNodeSet_hasEmptyPointersNodesValues_andBracketString() {
        BasicNodeSet set = new BasicNodeSet();

        assertTrue(set.getPointers().isEmpty());
        assertTrue(set.getNodes().isEmpty());
        assertTrue(set.getValues().isEmpty());

        assertEquals("[]", set.toString());
    }

    @Test
    public void getPointers_returnsSameReadOnlyInstanceAcrossCalls() {
        BasicNodeSet set = new BasicNodeSet();

        List<Pointer> first = set.getPointers();
        List<Pointer> second = set.getPointers();

        // Cached instance is reused
        assertSame(first, second);

        // Returned view is read-only
        assertThrows(UnsupportedOperationException.class, () -> first.add(null));
    }

    // add/remove Pointer

    @Test
    public void addAndRemovePointer_updatesPointersList() {
        BasicNodeSet set = new BasicNodeSet();
        Pointer p = newVarPointer("x");

        set.add(p);
        assertEquals(1, set.getPointers().size());
        assertSame(p, set.getPointers().get(0));

        set.remove(p);
        assertTrue(set.getPointers().isEmpty());
    }

    @Test
    public void removeNullPointer_isNoOp() {
        BasicNodeSet set = new BasicNodeSet();

        // Should not throw
        set.remove(null);
        assertTrue(set.getPointers().isEmpty());

        // Also safe after adding/removing real pointers
        Pointer p = newVarPointer("x");
        set.add(p);
        set.remove(null); // still a no-op
        assertEquals(1, set.getPointers().size());
    }

    // add(NodeSet ...)

    @Test
    public void addNullNodeSet_throwsNullPointerException() {
        BasicNodeSet set = new BasicNodeSet();
        assertThrows(NullPointerException.class, () -> set.add((NodeSet) null));
    }

    @Test
    public void addAnotherNodeSet_mergesPointers() {
        BasicNodeSet target = new BasicNodeSet();
        BasicNodeSet source = new BasicNodeSet();

        Pointer p1 = newVarPointer("a");
        Pointer p2 = newVarPointer("b");

        target.add(p1);
        source.add(p2);

        target.add(source);

        List<Pointer> pointers = target.getPointers();
        assertEquals(2, pointers.size());
        assertTrue(pointers.containsAll(Arrays.asList(p1, p2)));
    }

    @Test
    public void addSelf_onEmptySet_isNoOp() {
        BasicNodeSet set = new BasicNodeSet();

        // Should not throw and should remain empty
        set.add(set);

        assertTrue(set.getPointers().isEmpty());
        assertTrue(set.getNodes().isEmpty());
        assertTrue(set.getValues().isEmpty());
    }
}