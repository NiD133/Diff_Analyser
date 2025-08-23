/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TreeBag}.
 */
public class TreeBagTest extends AbstractSortedBagTest<String> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public SortedBag<String> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Creates a new sorted bag pre-populated with the given elements.
     */
    private SortedBag<String> newStringBag(final String... elements) {
        final SortedBag<String> bag = makeObject();
        for (final String e : elements) {
            bag.add(e);
        }
        return bag;
    }

    @Test
    void add_nonComparableToNaturallyOrderedBag_throwsIllegalArgumentException() {
        // See COLLECTIONS-265: Natural ordering requires Comparable elements.
        final Bag<Object> bag = new TreeBag<>();

        assertThrows(IllegalArgumentException.class, () -> bag.add(new Object()));
    }

    @Test
    void add_nullToNaturallyOrderedBag_throwsNullPointerException() {
        // See COLLECTIONS-555: Natural ordering in TreeMap does not permit null keys.
        final Bag<Object> bag = new TreeBag<>();

        assertThrows(NullPointerException.class, () -> bag.add(null));
    }

    @Test
    void add_nullToComparatorBackedBag_throwsNullPointerException_evenWhenNotEmpty() {
        // See COLLECTIONS-555:
        // Historically, adding null to an empty TreeMap with a custom comparator may "succeed"
        // because no comparison is needed yet. Ensure the bag is non-empty so a comparison occurs.
        final Bag<String> bag = new TreeBag<>(String::compareTo);
        bag.add("a"); // make non-empty

        assertThrows(NullPointerException.class, () -> bag.add(null));
    }

    @Test
    void iterationAndEndpoints_followNaturalOrdering() {
        // Given a bag with elements in unsorted insertion order
        final SortedBag<String> bag = newStringBag("C", "A", "B", "D");

        // Then iteration order should be natural (sorted) order
        assertArrayEquals(new Object[] {"A", "B", "C", "D"}, bag.toArray(), "Elements should be returned in sorted order");

        // And first/last should reflect natural ordering
        assertEquals("A", bag.first(), "first() should return the smallest element");
        assertEquals("D", bag.last(), "last() should return the largest element");
    }

//    // Helper to (re)generate serialized compatibility resources.
//    // Run manually when updating test serialization forms.
//    void testCreate() throws Exception {
//        Bag<String> bag = makeObject();
//        writeExternalFormToDisk((java.io.Serializable) bag, "src/test/resources/data/test/TreeBag.emptyCollection.version4.obj");
//        bag = makeFullCollection();
//        writeExternalFormToDisk((java.io.Serializable) bag, "src/test/resources/data/test/TreeBag.fullCollection.version4.obj");
//    }
}