package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Focused tests for OptionFormatter behavior.
 * 
 * The tests are organized by feature to improve readability:
 * - Formatting helpers (optional text, arg names, opt/long-opt combinations)
 * - Syntax output (default and custom)
 * - Deprecation formatting
 * - Builder configuration options
 * - Copy semantics and basic builder error conditions
 */
class OptionFormatterTest {

    private static final String SHORT = "o";
    private static final String LONG = "opt";
    private static final String OTHER = "other";

    // ----------------------------------------------------------------------
    // Test data providers
    // ----------------------------------------------------------------------

    /**
     * Test cases for Complex Deprecation formatting.
     */
    static Stream<Arguments> deprecatedAttributesData() {
        return Stream.of(
                Arguments.of(DeprecatedAttributes.builder().get(), "[Deprecated]"),
                Arguments.of(DeprecatedAttributes.builder().setSince("now").get(), "[Deprecated since now]"),
                Arguments.of(DeprecatedAttributes.builder().setSince("now").setForRemoval(true).get(),
                        "[Deprecated for removal since now]"),
                Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).get(), "[Deprecated for removal]"),
                Arguments.of(DeprecatedAttributes.builder().setDescription("Use something else").get(),
                        "[Deprecated. Use something else]"),
                Arguments.of(DeprecatedAttributes.builder().setDescription("Use something else").setForRemoval(true).get(),
                        "[Deprecated for removal. Use something else]"),
                Arguments.of(DeprecatedAttributes.builder().setDescription("Use something else").setSince("then").get(),
                        "[Deprecated since then. Use something else]"),
                Arguments.of(DeprecatedAttributes.builder().setDescription("Use something else").setSince("then").setForRemoval(true).get(),
                        "[Deprecated for removal since then. Use something else]")
        );
    }

    /**
     * Test cases for default syntax formatting: toSyntaxOption() honors the option's required flag.
     */
    static Stream<Arguments> syntaxFormatData() {
        return Stream.of(
                // short + long, optional arg with default name
                Arguments.of("short + long, optional arg", opt(b -> b.option(SHORT).longOpt(LONG).hasArg()),
                        "[-o <arg>]"),
                // short + long, optional arg with custom name
                Arguments.of("short + long, optional arg named 'other'",
                        opt(b -> b.option(SHORT).longOpt(LONG).hasArg().argName(OTHER)),
                        "[-o <other>]"),
                // short + long, required arg with custom name
                Arguments.of("short + long, required arg named 'other'",
                        opt(b -> b.option(SHORT).longOpt(LONG).hasArg().required().argName(OTHER)),
                        "-o <other>"),
                // short required, no arg
                Arguments.of("short required, no arg", opt(b -> b.option(SHORT).longOpt(LONG).required().argName(OTHER)),
                        "-o"),
                // short optional, no arg
                Arguments.of("short optional, no arg", opt(b -> b.option(SHORT).argName(OTHER)),
                        "[-o]"),
                // long optional with arg name
                Arguments.of("long optional with arg", opt(b -> b.longOpt(LONG).hasArg().argName(OTHER)),
                        "[--opt <other>]"),
                // long required with arg name
                Arguments.of("long required with arg", opt(b -> b.longOpt(LONG).required().hasArg().argName(OTHER)),
                        "--opt <other>"),
                // multi-char short opt
                Arguments.of("multi-char short opt", opt(b -> b.option("ot").longOpt(LONG).hasArg()),
                        "[-ot <arg>]")
        );
    }

    // ----------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------

    private static Option opt(final Consumer<Option.Builder> customizer) {
        final Option.Builder b = Option.builder();
        customizer.accept(b);
        return b.get();
    }

    private static OptionFormatter fmt(final Option option) {
        return OptionFormatter.from(option);
    }

    private static void assertEquivalent(final OptionFormatter a, final OptionFormatter b) {
        assertEquals(a.toSyntaxOption(), b.toSyntaxOption());
        assertEquals(a.toSyntaxOption(true), b.toSyntaxOption(true));
        assertEquals(a.toSyntaxOption(false), b.toSyntaxOption(false));
        assertEquals(a.getOpt(), b.getOpt());
        assertEquals(a.getLongOpt(), b.getLongOpt());
        assertEquals(a.getBothOpt(), b.getBothOpt());
        assertEquals(a.getDescription(), b.getDescription());
        assertEquals(a.getArgName(), b.getArgName());
        assertEquals(a.toOptional("foo"), b.toOptional("foo"));
    }

    // ----------------------------------------------------------------------
    // Optional text formatting
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("toOptional wraps text with default or custom delimiters and ignores null/empty")
    void toOptional_wrapsText() {
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter underTest = fmt(option);
        assertEquals("[what]", underTest.toOptional("what"));
        assertEquals("", underTest.toOptional(""), "Empty string should return empty string");
        assertEquals("", underTest.toOptional(null), "null should return empty string");

        underTest = OptionFormatter.builder()
                .setOptionalDelimiters("-> ", " <-")
                .build(option);
        assertEquals("-> what <-", underTest.toOptional("what"));
    }

    // ----------------------------------------------------------------------
    // Syntax output
    // ----------------------------------------------------------------------

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("syntaxFormatData")
    void toSyntaxOption_defaultBehavior(final String name, final Option option, final String expected) {
        assertEquals(expected, fmt(option).toSyntaxOption(), name);
    }

    @Test
    @DisplayName("Default syntax format can be forced to required/optional")
    void toSyntaxOption_forcedRequiredFlag() {
        // optional by default
        Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());
        OptionFormatter formatter = fmt(option);
        assertEquals("[-o <arg>]", formatter.toSyntaxOption());
        assertEquals("-o <arg>", formatter.toSyntaxOption(true)); // force required

        // required by default
        option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg().required());
        formatter = fmt(option);
        assertEquals("-o <arg>", formatter.toSyntaxOption());
        assertEquals("[-o <arg>]", formatter.toSyntaxOption(false)); // force optional
    }

    // ----------------------------------------------------------------------
    // Basic builder error conditions (CLI-343)
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("CLI-343 Part 1: Builder without opt/longOpt should fail at build()/get()")
    void cli343_missingOptionPart1() {
        assertThrows(IllegalStateException.class, () -> Option.builder().required(false).build());
        assertThrows(IllegalStateException.class, () -> Option.builder().required(false).get());
    }

    @Test
    @DisplayName("CLI-343 Part 2: Builder with only description should fail at build()/get()")
    void cli343_missingOptionPart2() {
        assertThrows(IllegalStateException.class, () -> Option.builder().desc("description").build());
        assertThrows(IllegalStateException.class, () -> Option.builder().desc("description").get());
    }

    // ----------------------------------------------------------------------
    // Deprecation formatting
    // ----------------------------------------------------------------------

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("deprecatedAttributesData")
    @DisplayName("COMPLEX_DEPRECATED_FORMAT emits full deprecation details")
    void complexDeprecationFormat_emitsAllFields(final DeprecatedAttributes da, final String expected) {
        final Option.Builder base = Option.builder(SHORT).deprecated(da);
        final Option.Builder withDesc = Option.builder(SHORT).desc("The description").deprecated(da);

        assertEquals(expected, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(base.get()));
        assertEquals(expected + " The description", OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(withDesc.get()));
    }

    @Test
    @DisplayName("getDescription respects deprecated format function selection")
    void getDescription_honorsDeprecatedFormatFunction() {
        final Option normal = opt(b -> b.option(SHORT).longOpt("one").hasArg().desc("The description"));
        final Option deprecated = opt(b -> b.option(SHORT).longOpt("one").hasArg().desc("The description").deprecated());
        final Option complexDeprecated = opt(b -> b.option(SHORT).longOpt("one").hasArg().desc("The description")
                .deprecated(DeprecatedAttributes.builder()
                        .setForRemoval(true)
                        .setSince("now")
                        .setDescription("Use something else")
                        .get()));

        // Default (NO_DEPRECATED_FORMAT)
        assertEquals("The description", fmt(normal).getDescription(), "normal option");
        assertEquals("The description", fmt(deprecated).getDescription(), "deprecated option");
        assertEquals("The description", fmt(complexDeprecated).getDescription(), "complex deprecated option");

        // SIMPLE_DEPRECATED_FORMAT
        OptionFormatter.Builder builder = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT);
        assertEquals("The description", builder.build(normal).getDescription(), "normal option");
        assertEquals("[Deprecated] The description", builder.build(deprecated).getDescription(), "deprecated option");
        assertEquals("[Deprecated] The description", builder.build(complexDeprecated).getDescription(), "complex deprecated option");

        // COMPLEX_DEPRECATED_FORMAT
        builder = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);
        assertEquals("The description", builder.build(normal).getDescription(), "normal option");
        assertEquals("[Deprecated] The description", builder.build(deprecated).getDescription(), "deprecated option");
        assertEquals("[Deprecated for removal since now. Use something else] The description",
                builder.build(complexDeprecated).getDescription(),
                "complex deprecated option");
    }

    // ----------------------------------------------------------------------
    // Builder configuration knobs
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Argument name delimiters wrap arg names; null/empty reverts to defaults")
    void setArgumentNameDelimiters_formatsArgName() {
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter.Builder builder = OptionFormatter.builder()
                .setArgumentNameDelimiters("with argument named ", ".");
        assertEquals("with argument named arg.", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setArgumentNameDelimiters(null, "");
        assertEquals("arg", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setArgumentNameDelimiters("", null);
        assertEquals("arg", builder.build(option).getArgName());
    }

    @Test
    @DisplayName("Default arg name is used when option does not provide an arg name")
    void setDefaultArgName_appliesWhenMissing() {
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter.Builder builder = OptionFormatter.builder().setDefaultArgName("foo");
        assertEquals("<foo>", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setDefaultArgName("");
        assertEquals("<arg>", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setDefaultArgName(null);
        assertEquals("<arg>", builder.build(option).getArgName());
    }

    @Test
    @DisplayName("Long option prefix is applied; null/empty falls back to default")
    void setLongOptPrefix_appliesAndFallsBack() {
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter.Builder builder = OptionFormatter.builder().setLongOptPrefix("fo");
        assertEquals("foopt", builder.build(option).getLongOpt());

        builder = OptionFormatter.builder().setLongOptPrefix("");
        assertEquals("opt", builder.build(option).getLongOpt());

        builder = OptionFormatter.builder().setLongOptPrefix(null);
        assertEquals("opt", builder.build(option).getLongOpt());
    }

    @Test
    @DisplayName("Opt-arg separator appears between option and its argument")
    void setOptArgumentSeparator_applies() {
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter.Builder builder = OptionFormatter.builder().setOptArgSeparator(" with argument named ");
        assertEquals("[-o with argument named <arg>]", builder.build(option).toSyntaxOption());

        builder = OptionFormatter.builder().setOptArgSeparator(null);
        assertEquals("[-o<arg>]", builder.build(option).toSyntaxOption());

        builder = OptionFormatter.builder().setOptArgSeparator("=");
        assertEquals("[-o=<arg>]", builder.build(option).toSyntaxOption());
    }

    @Test
    @DisplayName("Short/long option separator is used when both are present; null/empty uses default behavior")
    void setOptSeparator_applies() {
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter.Builder builder = OptionFormatter.builder().setOptSeparator(" and ");
        assertEquals("-o and --opt", builder.build(option).getBothOpt());

        builder = OptionFormatter.builder().setOptSeparator("");
        assertEquals("-o--opt", builder.build(option).getBothOpt(), "Empty string should return default");

        builder = OptionFormatter.builder().setOptSeparator(null);
        assertEquals("-o--opt", builder.build(option).getBothOpt(), "null string should return default");
    }

    @Test
    @DisplayName("Custom syntax format function overrides default formatting; null restores default")
    void setSyntaxFormatFunction_overrides() {
        final BiFunction<OptionFormatter, Boolean, String> func = (o, b) -> "Yep, it worked";
        final Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());

        OptionFormatter.Builder builder = OptionFormatter.builder().setSyntaxFormatFunction(func);
        assertEquals("Yep, it worked", builder.build(option).toSyntaxOption());

        builder = OptionFormatter.builder().setSyntaxFormatFunction(null);
        assertEquals("[-o <arg>]", builder.build(option).toSyntaxOption());
    }

    // ----------------------------------------------------------------------
    // Accessors
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("getBothOpt joins short and long options based on availability")
    void getBothOpt_joinsShortAndLong() {
        Option option = opt(b -> b.option(SHORT).longOpt(LONG).hasArg());
        assertEquals("-o, --opt", fmt(option).getBothOpt());

        option = opt(b -> b.longOpt(LONG).hasArg());
        assertEquals("--opt", fmt(option).getBothOpt());

        option = opt(b -> b.option(SHORT).hasArg());
        assertEquals("-o", fmt(option).getBothOpt());
    }

    // ----------------------------------------------------------------------
    // Copy semantics
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Builder copy constructor produces equivalent formatters")
    void copyConstructor_preservesConfiguration() {
        final Function<Option, String> depFunc = o -> "Ooo Deprecated";
        final BiFunction<OptionFormatter, Boolean, String> fmtFunc = (o, b) -> "Yep, it worked";

        final OptionFormatter.Builder builder = OptionFormatter.builder()
                .setLongOptPrefix("l")
                .setOptPrefix("s")
                .setArgumentNameDelimiters("{", "}")
                .setDefaultArgName("Some Argument")
                .setOptSeparator(" and ")
                .setOptionalDelimiters("?>", "<?")
                .setSyntaxFormatFunction(fmtFunc)
                .setDeprecatedFormatFunction(depFunc);

        Option option = Option.builder(SHORT).longOpt(LONG).get();
        OptionFormatter formatter = builder.build(option);
        OptionFormatter.Builder copied = new OptionFormatter.Builder(formatter);
        assertEquivalent(formatter, copied.build(option));

        option = Option.builder(SHORT).longOpt(LONG).deprecated().required().get();
        formatter = builder.build(option);
        copied = new OptionFormatter.Builder(formatter);
        assertEquivalent(formatter, copied.build(option));
    }
}