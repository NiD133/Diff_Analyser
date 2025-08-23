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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TransformedBag}.
 * <p>
 * This suite intentionally uses Object as the element type to avoid unsafe casts
 * and to clearly demonstrate that:
 * - transformingBag(...) transforms elements only when they are added after decoration.
 * - transformedBag(...) transforms any existing elements in the decorated bag.
 * </p>
 */
public class TransformedBagTest extends AbstractBagTest<Object> {

    private static final String[] NUMERIC_STRINGS = {"1", "3", "5", "7", "2", "4", "6"};

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public Bag<Object> makeObject() {
        // A no-op transformer preserves added elements unchanged.
        return TransformedBag.transformingBag(new HashBag<>(),
                TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    /**
     * Helper to create a bag that transforms added String values to Integer values.
     */
    private Bag<Object> newTransformingIntegerBag() {
        return TransformedBag.transformingBag(new HashBag<>(),
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
    }

    @Test
    void transformingBag_transformsNewlyAddedElementsOnly() {
        final Bag<Object> bag = newTransformingIntegerBag();

        assertTrue(bag.isEmpty(), "Newly created bag should be empty");

        for (int i = 0; i < NUMERIC_STRINGS.length; i++) {
            final String value = NUMERIC_STRINGS[i];

            // Add a String; the bag should store the transformed Integer.
            bag.add(value);

            assertEquals(i + 1, bag.size(), "Size should reflect number of additions");
            assertTrue(bag.contains(Integer.valueOf(value)), "Bag should contain transformed Integer");
            assertFalse(bag.contains(value), "Bag should not contain the original String");
        }

        // Removing the original String should fail; removing the transformed Integer should succeed.
        assertFalse(bag.remove(NUMERIC_STRINGS[0]));
        assertTrue(bag.remove(Integer.valueOf(NUMERIC_STRINGS[0])));
    }

    @Test
    void transformedBag_transformsExistingContents() {
        // Populate a plain bag with Strings first.
        final Bag<Object> original = new HashBag<>();
        for (final String s : NUMERIC_STRINGS) {
            original.add(s);
        }

        // Decorating with transformedBag(...) should transform existing contents immediately.
        final Bag<Object> bag = TransformedBag.transformedBag(original,
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);

        assertEquals(NUMERIC_STRINGS.length, bag.size(), "All original elements should remain after transformation");

        for (final String s : NUMERIC_STRINGS) {
            assertTrue(bag.contains(Integer.valueOf(s)), "Bag should contain transformed Integer");
            assertFalse(bag.contains(s), "Bag should not contain the original String after transformation");
        }

        // Removing the original String should fail; removing the transformed Integer should succeed.
        assertFalse(bag.remove(NUMERIC_STRINGS[0]));
        assertTrue(bag.remove(Integer.valueOf(NUMERIC_STRINGS[0])));
    }

//    // Utility to regenerate serialized compatibility resources, if needed.
//    // Disabled by default to avoid accidental file writes in CI.
//    void testCreate() throws Exception {
//        Bag<Object> bag = makeObject();
//        writeExternalFormToDisk((java.io.Serializable) bag, "src/test/resources/data/test/TransformedBag.emptyCollection.version4.obj");
//        bag = makeFullCollection();
//        writeExternalFormToDisk((java.io.Serializable) bag, "src/test/resources/data/test/TransformedBag.fullCollection.version4.obj");
//    }
}