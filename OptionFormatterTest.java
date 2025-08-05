package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link OptionFormatter}.
 */
class OptionFormatterTest {

    /**
     * Provides test data for deprecated attributes.
     *
     * @return a stream of arguments for parameterized tests.
     */
    public static Stream<Arguments> provideDeprecatedAttributesData() {
        List<Arguments> argumentsList = new ArrayList<>();
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();

        argumentsList.add(Arguments.of(builder.get(), "[Deprecated]"));

        builder.setSince("now");
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated since now]"));

        builder.setForRemoval(true);
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated for removal since now]"));

        builder.setSince(null);
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated for removal]"));

        builder.setForRemoval(false).setDescription("Use something else");
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated. Use something else]"));

        builder.setForRemoval(true);
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated for removal. Use something else]"));

        builder.setForRemoval(false).setSince("then");
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated since then. Use something else]"));

        builder.setForRemoval(true);
        argumentsList.add(Arguments.of(builder.get(), "[Deprecated for removal since then. Use something else]"));

        return argumentsList.stream();
    }

    /**
     * Asserts that two {@link OptionFormatter} instances are equivalent.
     *
     * @param formatter1 the first formatter
     * @param formatter2 the second formatter
     */
    private void assertFormattersAreEquivalent(OptionFormatter formatter1, OptionFormatter formatter2) {
        assertEquals(formatter1.toSyntaxOption(), formatter2.toSyntaxOption());
        assertEquals(formatter1.toSyntaxOption(true), formatter2.toSyntaxOption(true));
        assertEquals(formatter1.toSyntaxOption(false), formatter2.toSyntaxOption(false));
        assertEquals(formatter1.getOpt(), formatter2.getOpt());
        assertEquals(formatter1.getLongOpt(), formatter2.getLongOpt());
        assertEquals(formatter1.getBothOpt(), formatter2.getBothOpt());
        assertEquals(formatter1.getDescription(), formatter2.getDescription());
        assertEquals(formatter1.getArgName(), formatter2.getArgName());
        assertEquals(formatter1.toOptional("foo"), formatter2.toOptional("foo"));
    }

    @Test
    void testOptionalArgumentFormatting() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter formatter = OptionFormatter.from(option);

        assertEquals("[what]", formatter.toOptional("what"));
        assertEquals("", formatter.toOptional(""), "Empty string should return empty string");
        assertEquals("", formatter.toOptional(null), "Null should return empty string");

        OptionFormatter customFormatter = OptionFormatter.builder()
            .setOptionalDelimiters("-> ", " <-")
            .build(option);
        assertEquals("-> what <-", customFormatter.toOptional("what"));
    }

    @Test
    void testSyntaxOptionFormatting() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter formatter = OptionFormatter.from(option);
        assertEquals("[-o <arg>]", formatter.toSyntaxOption(), "Optional argument failed");

        option = Option.builder().option("o").longOpt("opt").hasArg().argName("other").build();
        formatter = OptionFormatter.from(option);
        assertEquals("[-o <other>]", formatter.toSyntaxOption(), "Optional 'other' argument failed");

        option = Option.builder().option("o").longOpt("opt").hasArg().required().argName("other").build();
        formatter = OptionFormatter.from(option);
        assertEquals("-o <other>", formatter.toSyntaxOption(), "Required 'other' argument failed");

        option = Option.builder().option("o").longOpt("opt").required().argName("other").build();
        formatter = OptionFormatter.from(option);
        assertEquals("-o", formatter.toSyntaxOption(), "Required no argument failed");

        option = Option.builder().option("o").argName("other").build();
        formatter = OptionFormatter.from(option);
        assertEquals("[-o]", formatter.toSyntaxOption(), "Optional no argument failed");

        option = Option.builder().longOpt("opt").hasArg().argName("other").build();
        formatter = OptionFormatter.from(option);
        assertEquals("[--opt <other>]", formatter.toSyntaxOption(), "Optional longOpt 'other' argument failed");

        option = Option.builder().longOpt("opt").required().hasArg().argName("other").build();
        formatter = OptionFormatter.from(option);
        assertEquals("--opt <other>", formatter.toSyntaxOption(), "Required longOpt 'other' argument failed");

        option = Option.builder().option("ot").longOpt("opt").hasArg().build();
        formatter = OptionFormatter.from(option);
        assertEquals("[-ot <arg>]", formatter.toSyntaxOption(), "Optional multi-char opt argument failed");
    }

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("provideDeprecatedAttributesData")
    void testComplexDeprecationFormat(DeprecatedAttributes deprecatedAttributes, String expected) {
        Option.Builder builder = Option.builder("o").deprecated(deprecatedAttributes);
        Option.Builder builderWithDesc = Option.builder("o").desc("The description").deprecated(deprecatedAttributes);

        assertEquals(expected, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(builder.build()));
        assertEquals(expected + " The description", OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(builderWithDesc.build()));
    }

    @Test
    void testCopyConstructor() {
        Function<Option, String> deprecatedFunction = o -> "Ooo Deprecated";
        BiFunction<OptionFormatter, Boolean, String> formatFunction = (o, b) -> "Yep, it worked";

        OptionFormatter.Builder builder = OptionFormatter.builder()
            .setLongOptPrefix("l")
            .setOptPrefix("s")
            .setArgumentNameDelimiters("{", "}")
            .setDefaultArgName("Some Argument")
            .setOptSeparator(" and ")
            .setOptionalDelimiters("?>", "<?")
            .setSyntaxFormatFunction(formatFunction)
            .setDeprecatedFormatFunction(deprecatedFunction);

        Option option = Option.builder("o").longOpt("opt").build();
        OptionFormatter formatter = builder.build(option);
        OptionFormatter.Builder copiedBuilder = new OptionFormatter.Builder(formatter);
        assertFormattersAreEquivalent(formatter, copiedBuilder.build(option));

        option = Option.builder("o").longOpt("opt").deprecated().required().build();
        formatter = builder.build(option);
        copiedBuilder = new OptionFormatter.Builder(formatter);
        assertFormattersAreEquivalent(formatter, copiedBuilder.build(option));
    }

    @Test
    void testDefaultSyntaxFormat() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter formatter = OptionFormatter.from(option);
        assertEquals("[-o <arg>]", formatter.toSyntaxOption());
        assertEquals("-o <arg>", formatter.toSyntaxOption(true));

        option = Option.builder().option("o").longOpt("opt").hasArg().required().build();
        formatter = OptionFormatter.from(option);
        assertEquals("-o <arg>", formatter.toSyntaxOption());
        assertEquals("[-o <arg>]", formatter.toSyntaxOption(false));
    }

    @Test
    void testGetBothOpt() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter formatter = OptionFormatter.from(option);
        assertEquals("-o, --opt", formatter.getBothOpt());

        option = Option.builder().longOpt("opt").hasArg().build();
        formatter = OptionFormatter.from(option);
        assertEquals("--opt", formatter.getBothOpt());

        option = Option.builder().option("o").hasArg().build();
        formatter = OptionFormatter.from(option);
        assertEquals("-o", formatter.getBothOpt());
    }

    @Test
    void testGetDescription() {
        Option normalOption = Option.builder().option("o").longOpt("one").hasArg().desc("The description").build();
        Option deprecatedOption = Option.builder().option("o").longOpt("one").hasArg().desc("The description").deprecated().build();
        Option deprecatedOptionWithAttributes = Option.builder().option("o").longOpt("one").hasArg().desc("The description")
            .deprecated(DeprecatedAttributes.builder().setForRemoval(true).setSince("now").setDescription("Use something else").get()).build();

        assertEquals("The description", OptionFormatter.from(normalOption).getDescription(), "Normal option failure");
        assertEquals("The description", OptionFormatter.from(deprecatedOption).getDescription(), "Deprecated option failure");
        assertEquals("The description", OptionFormatter.from(deprecatedOptionWithAttributes).getDescription(), "Complex deprecated option failure");

        OptionFormatter.Builder builder = OptionFormatter.builder().setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT);

        assertEquals("The description", builder.build(normalOption).getDescription(), "Normal option failure");
        assertEquals("[Deprecated] The description", builder.build(deprecatedOption).getDescription(), "Deprecated option failure");
        assertEquals("[Deprecated] The description", builder.build(deprecatedOptionWithAttributes).getDescription(), "Complex deprecated option failure");

        builder = OptionFormatter.builder().setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);

        assertEquals("The description", builder.build(normalOption).getDescription(), "Normal option failure");
        assertEquals("[Deprecated] The description", builder.build(deprecatedOption).getDescription(), "Deprecated option failure");
        assertEquals("[Deprecated for removal since now. Use something else] The description", builder.build(deprecatedOptionWithAttributes).getDescription(),
            "Complex deprecated option failure");
    }

    @Test
    void testSetArgumentNameDelimiters() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter.Builder builder = OptionFormatter.builder().setArgumentNameDelimiters("with argument named ", ".");
        assertEquals("with argument named arg.", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setArgumentNameDelimiters(null, "");
        assertEquals("arg", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setArgumentNameDelimiters("", null);
        assertEquals("arg", builder.build(option).getArgName());
    }

    @Test
    void testSetDefaultArgName() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter.Builder builder = OptionFormatter.builder().setDefaultArgName("foo");
        assertEquals("<foo>", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setDefaultArgName("");
        assertEquals("<arg>", builder.build(option).getArgName());

        builder = OptionFormatter.builder().setDefaultArgName(null);
        assertEquals("<arg>", builder.build(option).getArgName());
    }

    @Test
    void testSetLongOptPrefix() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter.Builder builder = OptionFormatter.builder().setLongOptPrefix("fo");
        assertEquals("foopt", builder.build(option).getLongOpt());

        builder = OptionFormatter.builder().setLongOptPrefix("");
        assertEquals("opt", builder.build(option).getLongOpt());

        builder = OptionFormatter.builder().setLongOptPrefix(null);
        assertEquals("opt", builder.build(option).getLongOpt());
    }

    @Test
    void testSetOptArgumentSeparator() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter.Builder builder = OptionFormatter.builder().setOptArgSeparator(" with argument named ");
        assertEquals("[-o with argument named <arg>]", builder.build(option).toSyntaxOption());

        builder = OptionFormatter.builder().setOptArgSeparator(null);
        assertEquals("[-o<arg>]", builder.build(option).toSyntaxOption());

        builder = OptionFormatter.builder().setOptArgSeparator("=");
        assertEquals("[-o=<arg>]", builder.build(option).toSyntaxOption());
    }

    @Test
    void testSetOptSeparator() {
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();
        OptionFormatter.Builder builder = OptionFormatter.builder().setOptSeparator(" and ");
        assertEquals("-o and --opt", builder.build(option).getBothOpt());

        builder = OptionFormatter.builder().setOptSeparator("");
        assertEquals("-o--opt", builder.build(option).getBothOpt(), "Empty string should return default");

        builder = OptionFormatter.builder().setOptSeparator(null);
        assertEquals("-o--opt", builder.build(option).getBothOpt(), "Null string should return default");
    }

    @Test
    void testSetSyntaxFormatFunction() {
        BiFunction<OptionFormatter, Boolean, String> customFunction = (o, b) -> "Yep, it worked";
        Option option = Option.builder().option("o").longOpt("opt").hasArg().build();

        OptionFormatter.Builder builder = OptionFormatter.builder().setSyntaxFormatFunction(customFunction);
        assertEquals("Yep, it worked", builder.build(option).toSyntaxOption());

        builder = OptionFormatter.builder().setSyntaxFormatFunction(null);
        assertEquals("[-o <arg>]", builder.build(option).toSyntaxOption());
    }
}