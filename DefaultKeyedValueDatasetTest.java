/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ---------------------------------
 * DefaultKeyedValueDatasetTest.java
 * ---------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    // ===========================================================
    // Tests for Object Equality
    // ===========================================================

    @Test
    public void identicalKeyValuePairs_ShouldBeEqual() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Test", 45.5);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Test", 45.5);
        assertEquals(dataset1, dataset2, "Datasets with same key and value should be equal");
    }

    @Test
    public void differentKeys_ShouldNotBeEqual() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Key1", 45.5);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Key2", 45.5);
        assertNotEquals(dataset1, dataset2, "Datasets with different keys should not be equal");
    }

    @Test
    public void differentValues_ShouldNotBeEqual() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Test", 45.5);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Test", 45.6);
        assertNotEquals(dataset1, dataset2, "Datasets with different values should not be equal");
    }

    // ===========================================================
    // Tests for Cloning Functionality
    // ===========================================================

    @Test
    public void cloning_ShouldCreateDistinctButEqualInstance() throws CloneNotSupportedException {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Test", 45.5);
        DefaultKeyedValueDataset clone = CloneUtils.clone(original);

        // Verify clone is a separate instance with identical state
        assertNotSame(original, clone, "Clone should be a distinct object");
        assertEquals(original, clone, "Clone should be equal to the original");
        assertEquals(original.getClass(), clone.getClass(), "Clone should have the same class");
    }

    // ===========================================================
    // Tests for Clone Independence
    // ===========================================================

    @Test
    public void cloneModification_ShouldNotAffectOriginal() throws CloneNotSupportedException {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Key", 10.0);
        DefaultKeyedValueDataset clone = CloneUtils.clone(original);

        // Modify clone and verify it diverges from original
        clone.updateValue(99.9);
        assertNotEquals(original, clone, "Clone should not equal original after modification");

        // Restore clone's value and verify equality is reestablished
        clone.updateValue(10.0);
        assertEquals(original, clone, "Equality should be restored after reverting clone");
    }

    // ===========================================================
    // Tests for Serialization
    // ===========================================================

    @Test
    public void serialization_ShouldPreserveObjectState() {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Test", 25.3);
        DefaultKeyedValueDataset deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized object should match original");
    }
}