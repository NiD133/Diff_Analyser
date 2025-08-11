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
 * --------------------------
 * DeviationRendererTest.java
 * --------------------------
 * Tests for the DeviationRenderer class.
 */

package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DeviationRenderer}.
 *
 * The tests focus on:
 * - equals/hashCode contract
 * - cloning semantics
 * - PublicCloneable implementation
 * - Java serialization round-trip
 */
@DisplayName("DeviationRenderer")
public class DeviationRendererTest {

    // -------- Helper methods to keep individual tests concise --------

    private static DeviationRenderer newRenderer() {
        return new DeviationRenderer();
    }

    private static DeviationRenderer rendererWithAlpha(float alpha) {
        DeviationRenderer r = newRenderer();
        r.setAlpha(alpha);
        return r;
    }

    // ------------------------------ equals ------------------------------

    @Test
    @DisplayName("equals: default instances are equal and symmetric")
    public void equals_defaultInstancesAreEqual() {
        DeviationRenderer r1 = newRenderer();
        DeviationRenderer r2 = newRenderer();

        assertAll(
                "Default instances should be equal and symmetric",
                () -> assertEquals(r1, r2, "r1 should equal r2"),
                () -> assertEquals(r2, r1, "r2 should equal r1")
        );
    }

    @Test
    @DisplayName("equals: differs when alpha differs, equal again when aligned")
    public void equals_detectsDifferenceInAlpha() {
        // Given two equal renderers
        DeviationRenderer r1 = newRenderer();
        DeviationRenderer r2 = newRenderer();
        assertEquals(r1, r2, "Precondition: default instances must be equal");

        // When alpha is changed on one
        r1.setAlpha(0.1f);

        // Then they are no longer equal
        assertNotEquals(r1, r2, "Different alpha should break equality");

        // When alpha is aligned
        r2.setAlpha(0.1f);

        // Then equality is restored
        assertEquals(r1, r2, "Setting the same alpha should restore equality");
    }

    // ---------------------------- hashCode -----------------------------

    @Test
    @DisplayName("hashCode: equal objects produce equal hash codes")
    public void hashCode_equalObjectsHaveSameHash() {
        DeviationRenderer r1 = newRenderer();
        DeviationRenderer r2 = newRenderer();

        assertEquals(r1, r2, "Precondition: instances must be equal");
        assertEquals(r1.hashCode(), r2.hashCode(), "Equal objects must have equal hash codes");
    }

    // ----------------------------- cloning -----------------------------

    @Test
    @DisplayName("clone: creates a distinct but equal copy")
    public void cloning_createsEqualButDistinctInstance() throws CloneNotSupportedException {
        DeviationRenderer original = rendererWithAlpha(0.3f);

        DeviationRenderer clone = (DeviationRenderer) original.clone();

        assertAll(
                "Clone should be a distinct but equal instance",
                () -> assertNotSame(original, clone, "Clone must be a different instance"),
                () -> assertSame(original.getClass(), clone.getClass(), "Clone must be of the same type"),
                () -> assertEquals(original, clone, "Clone must be equal to the original")
        );
    }

    // ------------------------ PublicCloneable --------------------------

    @Test
    @DisplayName("Implements PublicCloneable")
    public void implementsPublicCloneable() {
        assertTrue(newRenderer() instanceof PublicCloneable,
                "DeviationRenderer should implement PublicCloneable");
    }

    // -------------------------- serialization --------------------------

    @Test
    @DisplayName("Serialization: round-trip preserves equality")
    public void serialization_roundTripYieldsEqualObject() {
        DeviationRenderer original = rendererWithAlpha(0.25f);

        DeviationRenderer restored = TestUtils.serialised(original);

        assertEquals(original, restored, "Deserialized instance should equal the original");
    }
}