package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class, focusing on the
 * behavior of the equals() and clone() methods.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that the equals() method returns false after a cloned
     * CustomCategoryURLGenerator is modified. The original object and the
     * modified clone should no longer be considered equal.
     */
    @Test
    public void equals_shouldReturnFalse_whenClonedObjectIsModified() throws CloneNotSupportedException {
        // Arrange: Create an instance and its clone.
        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator clonedGenerator = (CustomCategoryURLGenerator) originalGenerator.clone();

        // Sanity checks: Ensure the clone is a different object but is initially equal.
        assertNotSame("The clone should be a different object in memory.", originalGenerator, clonedGenerator);
        assertTrue("A fresh clone should be equal to the original object.", clonedGenerator.equals(originalGenerator));

        // Act: Modify the cloned instance by adding a new URL series.
        List<String> newUrlSeries = new ArrayList<>();
        clonedGenerator.addURLSeries(newUrlSeries);

        // Assert: The original and the modified clone should no longer be equal.
        assertFalse("Original should not be equal to the modified clone.",
                    originalGenerator.equals(clonedGenerator));
        assertFalse("Modified clone should not be equal to the original (testing symmetry).",
                    clonedGenerator.equals(originalGenerator));
    }
}