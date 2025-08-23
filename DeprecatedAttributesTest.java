package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeprecatedAttributesTest {

    private static final String SAMPLE_DESCRIPTION = "Use Bar instead!";
    private static final String SAMPLE_SINCE = "2.0";

    @Test
    void builderAppliesNonDefaultValues() {
        final DeprecatedAttributes value = create(SAMPLE_DESCRIPTION, SAMPLE_SINCE, true);

        assertAll(
                () -> assertEquals(SAMPLE_DESCRIPTION, value.getDescription()),
                () -> assertEquals(SAMPLE_SINCE, value.getSince()),
                () -> assertTrue(value.isForRemoval())
        );
    }

    @Test
    void toString_withDescriptionSinceAndForRemoval() {
        final DeprecatedAttributes value = create(SAMPLE_DESCRIPTION, SAMPLE_SINCE, true);
        assertEquals("Deprecated for removal since 2.0: Use Bar instead!", value.toString());
    }

    @Test
    void toString_withDescriptionAndForRemovalOnly() {
        final DeprecatedAttributes value = create(SAMPLE_DESCRIPTION, "", true);
        assertEquals("Deprecated for removal: Use Bar instead!", value.toString());
    }

    @Test
    void toString_withDescriptionAndSinceOnly() {
        final DeprecatedAttributes value = create(SAMPLE_DESCRIPTION, SAMPLE_SINCE, false);
        assertEquals("Deprecated since 2.0: Use Bar instead!", value.toString());
    }

    @Test
    void toString_withDescriptionOnly() {
        final DeprecatedAttributes value = withDescription(SAMPLE_DESCRIPTION);
        assertEquals("Deprecated: Use Bar instead!", value.toString());
    }

    @Test
    void builderWithoutSettersUsesDefaultValues() {
        final DeprecatedAttributes built = DeprecatedAttributes.builder().get();

        assertAll(
                () -> assertEquals(DeprecatedAttributes.DEFAULT.getDescription(), built.getDescription()),
                () -> assertEquals(DeprecatedAttributes.DEFAULT.getSince(), built.getSince()),
                () -> assertEquals(DeprecatedAttributes.DEFAULT.isForRemoval(), built.isForRemoval())
        );
    }

    @Test
    void defaultToString_isJustDeprecated() {
        assertEquals("Deprecated", DeprecatedAttributes.DEFAULT.toString());
    }

    // Helpers

    private DeprecatedAttributes create(final String description, final String since, final boolean forRemoval) {
        return DeprecatedAttributes.builder()
                .setDescription(description)
                .setSince(since)
                .setForRemoval(forRemoval)
                .get();
    }

    private DeprecatedAttributes withDescription(final String description) {
        return DeprecatedAttributes.builder()
                .setDescription(description)
                .get();
    }
}