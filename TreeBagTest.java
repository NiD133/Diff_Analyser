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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link TreeBag} implementation, extending {@link AbstractSortedBagTest}.
 */
public class TreeBagTest<T> extends AbstractSortedBagTest<T> {

    /**
     * Returns the compatibility version for serialization tests.
     */
    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Creates and returns a new instance of {@link TreeBag}.
     */
    @Override
    public SortedBag<T> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Sets up a sample {@link SortedBag} with predefined elements for testing.
     */
    @SuppressWarnings("unchecked")
    private SortedBag<T> createSampleBag() {
        final SortedBag<T> bag = makeObject();
        bag.add((T) "C");
        bag.add((T) "A");
        bag.add((T) "B");
        bag.add((T) "D");
        return bag;
    }

    /**
     * Tests that adding a non-comparable object to a {@link TreeBag} throws an {@link IllegalArgumentException}.
     */
    @Test
    void testAddNonComparableObject() {
        final Bag<Object> bag = new TreeBag<>();
        assertThrows(IllegalArgumentException.class, () -> bag.add(new Object()));
    }

    /**
     * Tests that adding a null element to a {@link TreeBag} throws a {@link NullPointerException}.
     */
    @Test
    void testAddNullElement() {
        final Bag<Object> bag = new TreeBag<>();
        assertThrows(NullPointerException.class, () -> bag.add(null));

        final Bag<String> bagWithComparator = new TreeBag<>(String::compareTo);
        // Ensure the bag is not empty before adding null to avoid JDK bug with TreeMap
        bagWithComparator.add("a");
        assertThrows(NullPointerException.class, () -> bagWithComparator.add(null));
    }

    /**
     * Tests the ordering of elements in a {@link TreeBag}.
     */
    @Test
    void testElementOrdering() {
        final Bag<T> bag = createSampleBag();
        assertEquals("A", bag.toArray()[0], "First element should be 'A'");
        assertEquals("B", bag.toArray()[1], "Second element should be 'B'");
        assertEquals("C", bag.toArray()[2], "Third element should be 'C'");
        assertEquals("A", ((SortedBag<T>) bag).first(), "First element should be 'A'");
        assertEquals("D", ((SortedBag<T>) bag).last(), "Last element should be 'D'");
    }

    // Uncomment and implement if serialization tests are needed
    // @Test
    // void testSerialization() throws Exception {
    //     Bag<T> emptyBag = makeObject();
    //     writeExternalFormToDisk((java.io.Serializable) emptyBag, "src/test/resources/data/test/TreeBag.emptyCollection.version4.obj");
    //     Bag<T> fullBag = makeFullCollection();
    //     writeExternalFormToDisk((java.io.Serializable) fullBag, "src/test/resources/data/test/TreeBag.fullCollection.version4.obj");
    // }
}