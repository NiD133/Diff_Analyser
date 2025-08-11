package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for DeprecatedAttributes.
 *
 * These tests focus on:
 * - Default values (via DEFAULT and via the builder).
 * - Individual setters (description, since, forRemoval).
 * - Readable toString() output for the most common cases.
 *
 * Notes:
 * - We use the public builder() API instead of the deprecated Builder constructor.
 * - We keep each test small and focused to ease maintenance and readability.
 */
public class DeprecatedAttributesTest {

    private static final String SAMPLE_DESCRIPTION = "Use --new-option instead";
    private static final String SAMPLE_SINCE = "1.2.3";

    @Test
    public void defaultConstant_hasEmptyFieldsAndNotForRemoval() {
        DeprecatedAttributes defaults = DeprecatedAttributes.DEFAULT;

        assertEquals("Default description should be empty", "", defaults.getDescription());
        assertEquals("Default since should be empty", "", defaults.getSince());
        assertFalse("Default forRemoval should be false", defaults.isForRemoval());
    }

    @Test
    public void builder_defaultsMatchDEFAULT() {
        DeprecatedAttributes built = DeprecatedAttributes.builder().get();

        assertEquals("Description should default to empty", "", built.getDescription());
        assertEquals("Since should default to empty", "", built.getSince());
        assertFalse("forRemoval should default to false", built.isForRemoval());
    }

    @Test
    public void setForRemoval_true_setsFlagAndAffectsToString() {
        DeprecatedAttributes attrs = DeprecatedAttributes.builder()
                .setForRemoval(true)
                .get();

        assertTrue("forRemoval should be true after setting", attrs.isForRemoval());
        assertEquals("Unexpected toString() when only forRemoval is set",
                "Deprecated for removal", attrs.toString());
    }

    @Test
    public void setSince_setsValueAndAffectsToString() {
        DeprecatedAttributes attrs = DeprecatedAttributes.builder()
                .setSince(SAMPLE_SINCE)
                .get();

        assertEquals("since should reflect the value set", SAMPLE_SINCE, attrs.getSince());
        assertEquals("Unexpected toString() when only since is set",
                "Deprecated since " + SAMPLE_SINCE, attrs.toString());
    }

    @Test
    public void setDescription_setsValueAndAffectsToString() {
        DeprecatedAttributes attrs = DeprecatedAttributes.builder()
                .setDescription(SAMPLE_DESCRIPTION)
                .get();

        assertEquals("description should reflect the value set",
                SAMPLE_DESCRIPTION, attrs.getDescription());
        assertEquals("Unexpected toString() when only description is set",
                "Deprecated: " + SAMPLE_DESCRIPTION, attrs.toString());
    }

    @Test
    public void builder_isFluent_allFieldsCanBeSet() {
        DeprecatedAttributes attrs = DeprecatedAttributes.builder()
                .setDescription(SAMPLE_DESCRIPTION)
                .setSince(SAMPLE_SINCE)
                .setForRemoval(true)
                .get();

        assertEquals("description should be set", SAMPLE_DESCRIPTION, attrs.getDescription());
        assertEquals("since should be set", SAMPLE_SINCE, attrs.getSince());
        assertTrue("forRemoval should be set", attrs.isForRemoval());
        // Note: toString() precedence between description/since/forRemoval is tested individually above.
    }

    @Test
    public void minimalDescription_valueIsReflectedInToString() {
        DeprecatedAttributes attrs = DeprecatedAttributes.builder()
                .setDescription(",")
                .get();

        assertEquals(",", attrs.getDescription());
        assertEquals("Deprecated: ,", attrs.toString());
    }
}