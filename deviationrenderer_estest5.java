package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite contains tests for the {@link DeviationRenderer} class.
 * This specific test was improved for clarity from an auto-generated version.
 */
public class DeviationRenderer_ESTestTest5 extends DeviationRenderer_ESTest_scaffolding {

    /**
     * Verifies that getAlpha() returns the exact value of the internal 'alpha' field.
     *
     * This test case specifically checks the getter's behavior when the field is set
     * to a value outside the conventional range (0.0f to 1.0f) via direct field access.
     * This ensures the getter simply returns the field's current state without any
     * validation or modification.
     */
    @Test
    public void getAlpha_shouldReturnTheRawValueFromAlphaField() {
        // Arrange: Create a renderer and directly set its protected 'alpha' field
        // to an unconventional negative value.
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        float expectedAlpha = -189.0F;
        renderer.alpha = expectedAlpha;

        // Act: Call the getAlpha() method to retrieve the value.
        float actualAlpha = renderer.getAlpha();

        // Assert: The returned value should be identical to the value set on the field.
        assertEquals("The getter should return the exact raw value of the alpha field.",
                expectedAlpha, actualAlpha, 0.01F);
    }
}