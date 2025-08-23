package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.apache.commons.cli.help.OptionFormatter.*;
import static org.junit.jupiter.api.Assertions.*;

class OptionFormatterTest {

    // ---------------------------------------------------------------------
    // Basic formatting of opt/longOpt with custom prefixes and separators
    // ---------------------------------------------------------------------

    @Test
    void getLongOpt_usesCustomLongPrefix() {
        // Arrange
        Option option = new Option("?Wf", "?Wf", true, "?Wf");
        OptionFormatter formatter = OptionFormatter
                .builder()
                .setLongOptPrefix("?Wf")
                .build(option);

        // Act
        String longOpt = formatter.getLongOpt();

        // Assert
        assertEquals("?Wf?Wf", longOpt);
    }

    @Test
    void toSyntaxOption_usesCustomOptArgSeparator() {
        // Arrange
        Option option = new Option(null, null, true, null);
        OptionFormatter formatter = OptionFormatter
                .builder()
                .setOptArgSeparator("Deprecated")
                .build(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("[Deprecated<arg>]", syntax);
    }

    @Test
    void toSyntaxOption_requiredDoesNotUseOptionalDelimiters() {
        // Arrange
        Option option = new Option("?Wf", "?Wf", true, "");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String syntax = formatter.toSyntaxOption(true);

        // Assert
        assertEquals("-?Wf <arg>", syntax);
    }

    // ---------------------------------------------------------------------
    // Argument name behavior
    // ---------------------------------------------------------------------

    @Test
    void toSyntaxOption_usesDefaultArgNameWhenNotProvided() {
        // Arrange
        Option option = new Option(null, null, true, null);
        OptionFormatter formatter = OptionFormatter
                .builder()
                .setDefaultArgName("[Deprecated")
                .build(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("[ <[Deprecated>]", syntax);
    }

    @Test
    void getArgName_defaultsToArgWhenHasArgButNoName() {
        // Arrange
        Option option = new Option(null, null, true, "");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String argName = formatter.getArgName();

        // Assert
        assertEquals("<arg>", argName);
    }

    @Test
    void getArgName_isEmptyWhenOptionHasNoArgs() {
        // Arrange
        Option option = new Option(null, null);
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String argName = formatter.getArgName();

        // Assert
        assertEquals("", argName);
    }

    // ---------------------------------------------------------------------
    // Custom syntax function
    // ---------------------------------------------------------------------

    @Test
    void toSyntaxOption_canBeOverriddenToReturnNull() {
        // Arrange
        Option option = new Option("x", "x");
        BiFunction<OptionFormatter, Boolean, String> custom = (o, required) -> null;
        OptionFormatter formatter = OptionFormatter
                .builder()
                .setSyntaxFormatFunction(custom)
                .build(option);

        // Act + Assert
        assertNull(formatter.toSyntaxOption());
        assertNull(formatter.toSyntaxOption(false));
    }

    // ---------------------------------------------------------------------
    // Accessors: opt, longOpt, bothOpt, description, required
    // ---------------------------------------------------------------------

    @Test
    void getOpt_returnsShortOptWithDashOrEmpty() {
        // Arrange
        Option withShort = new Option("7", "desc", false, "");
        OptionFormatter f1 = OptionFormatter.from(withShort);

        Option noNames = new Option(null, null);
        OptionFormatter f2 = OptionFormatter.from(noNames);

        // Act + Assert
        assertEquals("-7", f1.getOpt());
        assertEquals("", f2.getOpt());
    }

    @Test
    void getLongOpt_isEmptyWhenOnlyShortOptProvided() {
        // Arrange
        Option option = new Option("o", "o"); // short= "o", description="o"
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act + Assert
        assertEquals("", formatter.getLongOpt());
    }

    @Test
    void getBothOpt_handlesShortOnly_both_none() {
        // Arrange
        Option shortOnly = new Option("?Wf", "?Wf");
        OptionFormatter fs = OptionFormatter.from(shortOnly);

        Option noNames = new Option(null, null);
        OptionFormatter fn = OptionFormatter.from(noNames);

        Option both = new Option("?Wf", "?Wf", true, "");
        OptionFormatter fb = OptionFormatter.from(both);

        // Act + Assert
        assertEquals("-?Wf", fs.getBothOpt());
        assertEquals("", fn.getBothOpt());
        assertEquals("-?Wf, --?Wf", fb.getBothOpt());
    }

    @Test
    void getDescription_returnsEmptyWhenNull_orProvidedDescription() {
        // Arrange
        Option noDesc = new Option(null, null);
        OptionFormatter f1 = OptionFormatter.from(noDesc);

        Option withDesc = new Option("short", "iRi[{-|Um");
        OptionFormatter f2 = OptionFormatter.from(withDesc);

        // Act + Assert
        assertEquals("", f1.getDescription());
        assertEquals("iRi[{-|Um", f2.getDescription());
    }

    @Test
    void isRequired_reflectsUnderlyingOption() {
        // Arrange
        Option option = new Option("?Wf", true, "?Wf");
        option.setRequired(true);
        OptionFormatter required = OptionFormatter.from(option);

        Option notRequiredOpt = new Option(null, null, true, "");
        OptionFormatter notRequired = OptionFormatter.from(notRequiredOpt);

        // Act + Assert
        assertTrue(required.isRequired());
        assertFalse(notRequired.isRequired());
    }

    // ---------------------------------------------------------------------
    // Syntax rendering with missing names
    // ---------------------------------------------------------------------

    @Test
    void toSyntaxOption_isEmptyWhenNoNamesAndNoArgs() {
        // Arrange
        Option option = new Option(null, null); // no args by default
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act + Assert
        assertEquals("", formatter.toSyntaxOption(false));
        assertEquals("", formatter.toSyntaxOption());
    }

    // ---------------------------------------------------------------------
    // toOptional wrapping
    // ---------------------------------------------------------------------

    @Test
    void toOptional_wrapsNonEmptyText_ignoresEmpty() {
        // Arrange
        Option someOption = new Option(null, null);
        OptionFormatter f1 = OptionFormatter.from(someOption);

        OptionFormatter f2 = OptionFormatter.from(null); // null option still supports toOptional()

        // Act + Assert
        assertEquals("", f1.toOptional(""));       // empty in => empty out
        assertEquals("[, ]", f2.toOptional(", ")); // non-empty wraps with brackets
    }

    // ---------------------------------------------------------------------
    // Since value
    // ---------------------------------------------------------------------

    @Test
    void getSince_defaultsToDoubleDashWhenUnset() {
        // Arrange
        Option option = new Option(null, null, true, null);
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act + Assert
        assertEquals("--", formatter.getSince());
    }

    // ---------------------------------------------------------------------
    // Builder API: chaining, helpers, supplier
    // ---------------------------------------------------------------------

    @Test
    void builder_supportsChaining_andHelperToArgName() {
        // Arrange
        Option option = new Option(null, "some description");
        OptionFormatter base = OptionFormatter.from(option);

        OptionFormatter.Builder builder = new OptionFormatter.Builder(base);

        // Act: verify fluent chaining returns same instance
        assertSame(builder, builder.setOptionalDelimiters("[", "]"));
        assertSame(builder, builder.setArgumentNameDelimiters("<", ">"));
        assertSame(builder, builder.setOptSeparator(", "));
        assertSame(builder, builder.setOptPrefix("-"));
        assertSame(builder, builder.setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT));

        // Helper toArgName() should respect delimiters
        assertEquals("<name>", builder.toArgName("name"));
    }

    @Test
    void builder_getAsSupplier_currentlyReturnsNull() {
        // Arrange
        OptionFormatter formatter = OptionFormatter.from(new Option(null, null));
        OptionFormatter.Builder builder = new OptionFormatter.Builder(formatter);

        // Act + Assert
        assertNull(builder.get(), "Builder#get is expected to return null per current implementation");
    }

    // ---------------------------------------------------------------------
    // Null Option behavior
    // ---------------------------------------------------------------------

    @Test
    void mostOperationsThrowWhenBuiltFromNullOption_butToOptionalWorks() {
        // Arrange
        OptionFormatter formatter = OptionFormatter.from(null);

        // Act + Assert
        assertThrows(NullPointerException.class, formatter::toSyntaxOption);
        assertThrows(NullPointerException.class, () -> formatter.toSyntaxOption(true));
        assertThrows(NullPointerException.class, formatter::isRequired);
        assertThrows(NullPointerException.class, formatter::getOpt);
        assertThrows(NullPointerException.class, formatter::getLongOpt);
        assertThrows(NullPointerException.class, formatter::getDescription);
        assertThrows(NullPointerException.class, formatter::getBothOpt);
        assertThrows(NullPointerException.class, formatter::getArgName);
        assertThrows(NullPointerException.class, formatter::getSince);

        // But toOptional() still works with non-empty input
        assertEquals("[, ]", formatter.toOptional(", "));
    }
}