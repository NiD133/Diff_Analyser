package org.jsoup.nodes;

import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.extended;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for verifying the case-sensitivity behavior of the {@link Entities} class.
 */
public class EntitiesCaseSensitivityTest {

    /**
     * Verifies that the {@link Entities#escape(String, Document.OutputSettings)} method
     * differentiates between uppercase and lowercase characters, mapping them to their
     * respective named entities. For example, 'Ü' is escaped to "&Uuml;" and 'ü' to "&uuml;".
     */
    @Test
    public void escapeDifferentiatesBetweenCharacterCases() {
        // Arrange
        String originalText = "Ü ü &";
        String expectedEscapedText = "&Uuml; &uuml; &amp;";

        // Use 'extended' escape mode to ensure 'Ü' and 'ü' are converted to named entities,
        // as they are not part of the 'base' entity set.
        OutputSettings settings = new OutputSettings()
            .charset("ascii")
            .escapeMode(extended);

        // Act
        String actualEscapedText = Entities.escape(originalText, settings);

        // Assert
        assertEquals(expectedEscapedText, actualEscapedText);
    }

    /**
     * Verifies that the {@link Entities#unescape(String)} method treats HTML entity names
     * in a case-insensitive manner. This is a lenient interpretation of the HTML5 spec,
     * which states that most entities are case-sensitive.
     *
     * This test confirms that standard mixed-case entities ("&Uuml;", "&uuml;"),
     * lowercase entities ("&amp;"), and non-standard uppercase entities ("&AMP")
     * are all correctly unescaped.
     */
    @Test
    public void unescapeTreatsEntityNamesCaseInsensitively() {
        // Arrange
        // Note: "&AMP" is not a standard HTML entity, but Jsoup's parser is lenient
        // and correctly interprets it as "&amp;".
        String textWithVariousCaseEntities = "&Uuml; &uuml; &amp; &AMP";
        String expectedUnescapedText = "Ü ü & &";

        // Act
        String actualUnescapedText = Entities.unescape(textWithVariousCaseEntities);

        // Assert
        assertEquals(expectedUnescapedText, actualUnescapedText);
    }
}