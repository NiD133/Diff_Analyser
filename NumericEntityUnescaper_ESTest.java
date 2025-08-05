package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    // An unescaper with default options (semiColonRequired)
    private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // --- Tests for Default Behavior (Semicolon Required) ---

    @Test
    public void shouldTranslateDecimalEntity() {
        assertEquals("Character A", unescaper.translate("Character &#65;"));
    }

    @Test
    public void shouldTranslateHexEntityWithLowercaseX() {
        assertEquals("Character B", unescaper.translate("Character &#x42;"));
    }

    @Test
    public void shouldTranslateHexEntityWithUppercaseX() {
        assertEquals("Character C", unescaper.translate("Character &#X43;"));
    }

    @Test
    public void shouldNotTranslateIfNoSemicolonWhenRequired() {
        assertEquals("Character &#65", unescaper.translate("Character &#65"));
    }

    @Test
    public void shouldNotTranslateMalformedEntity() {
        // Does not translate because 'y' is not a valid hex digit.
        assertEquals("test&#y;", unescaper.translate("test&#y;"));
    }

    @Test
    public void shouldNotTranslateIncompleteEntities() {
        assertEquals("test&", unescaper.translate("test&"));
        assertEquals("test&#", unescaper.translate("test&#"));
        assertEquals("test&#x", unescaper.translate("test&#x"));
    }

    @Test
    public void shouldNotTranslateNonNumericEntity() {
        assertEquals("test&thing;", unescaper.translate("test&thing;"));
    }

    @Test
    public void shouldTranslateSupplementaryCharacter() {
        // U+1F600 is a grinning face emoji 😀
        assertEquals("😀", unescaper.translate("&#128512;"));
        assertEquals("😀", unescaper.translate("&#x1F600;"));
    }

    // --- Tests for semiColonOptional Option ---

    @Test
    public void shouldTranslateEntityWithoutSemicolonWhenOptional() {
        NumericEntityUnescaper optionalUnescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertEquals("Character A", optionalUnescaper.translate("Character &#65"));
        assertEquals("Character B", optionalUnescaper.translate("Character &#x42"));
    }

    @Test
    public void shouldStillTranslateEntityWithSemicolonWhenOptional() {
        NumericEntityUnescaper optionalUnescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertEquals("Character C", optionalUnescaper.translate("Character &#X43;"));
    }

    // --- Tests for errorIfNoSemiColon Option ---

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForMissingSemicolonWhenErrorIsSet() {
        NumericEntityUnescaper errorUnescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        errorUnescaper.translate("test &#65 test");
    }

    @Test
    public void shouldTranslateWithSemicolonWhenErrorIsSet() {
        NumericEntityUnescaper errorUnescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertEquals("test A test", errorUnescaper.translate("test &#65; test"));
    }

    // --- Tests for translate(CharSequence, int, Writer) method ---

    @Test
    public void translateAtIndexShouldReturnZeroForNonEntityCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        // Start at index 1 ('e'), which is not an entity start.
        int consumedChars = unescaper.translate("test", 1, writer);

        assertEquals(0, consumedChars);
        assertEquals("", writer.toString());
    }

    @Test
    public void translateAtIndexShouldTranslateEntityAndReturnLength() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "foo&#65;bar";
        // Start at index 3 ('&')
        int consumedChars = unescaper.translate(input, 3, writer);

        assertEquals(5, consumedChars); // Length of "&#65;"
        assertEquals("A", writer.toString());
    }

    @Test
    public void translateAtIndexShouldReturnZeroForIncompleteEntityWhenSemicolonRequired() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "foo&#65"; // Missing semicolon
        int consumedChars = unescaper.translate(input, 3, writer);

        assertEquals(0, consumedChars);
        assertEquals("", writer.toString());
    }

    // --- Tests for Invalid Arguments and Edge Cases ---

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowExceptionForNullOptionInArray() {
        // Corresponds to the original test14
        new NumericEntityUnescaper(new OPTION[]{null});
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void translateAtIndexShouldThrowExceptionForNegativeIndex() throws IOException {
        unescaper.translate("test", -1, new StringWriter());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void translateAtIndexShouldThrowExceptionForIndexOutOfBounds() throws IOException {
        unescaper.translate("test", 5, new StringWriter());
    }

    @Test(expected = NullPointerException.class)
    public void translateAtIndexShouldThrowExceptionForNullInput() throws IOException {
        unescaper.translate(null, 0, new StringWriter());
    }
}