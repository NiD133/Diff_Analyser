package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

public class OptionFormatterTestTest8 {

    public static Stream<Arguments> deprecatedAttributesData() {
        final List<Arguments> lst = new ArrayList<>();
        final DeprecatedAttributes.Builder daBuilder = DeprecatedAttributes.builder();
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated]"));
        daBuilder.setSince("now");
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated since now]"));
        daBuilder.setForRemoval(true);
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated for removal since now]"));
        daBuilder.setSince(null);
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated for removal]"));
        daBuilder.setForRemoval(false);
        daBuilder.setDescription("Use something else");
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated. Use something else]"));
        daBuilder.setForRemoval(true);
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated for removal. Use something else]"));
        daBuilder.setForRemoval(false);
        daBuilder.setSince("then");
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated since then. Use something else]"));
        daBuilder.setForRemoval(true);
        lst.add(Arguments.of(daBuilder.get(), "[Deprecated for removal since then. Use something else]"));
        return lst.stream();
    }

    private void assertEquivalent(final OptionFormatter formatter, final OptionFormatter formatter2) {
        assertEquals(formatter.toSyntaxOption(), formatter2.toSyntaxOption());
        assertEquals(formatter.toSyntaxOption(true), formatter2.toSyntaxOption(true));
        assertEquals(formatter.toSyntaxOption(false), formatter2.toSyntaxOption(false));
        assertEquals(formatter.getOpt(), formatter2.getOpt());
        assertEquals(formatter.getLongOpt(), formatter2.getLongOpt());
        assertEquals(formatter.getBothOpt(), formatter2.getBothOpt());
        assertEquals(formatter.getDescription(), formatter2.getDescription());
        assertEquals(formatter.getArgName(), formatter2.getArgName());
        assertEquals(formatter.toOptional("foo"), formatter2.toOptional("foo"));
    }

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("deprecatedAttributesData")
    void testComplexDeprecationFormat(final DeprecatedAttributes da, final String expected) {
        final Option.Builder builder = Option.builder("o").deprecated(da);
        final Option.Builder builderWithDesc = Option.builder("o").desc("The description").deprecated(da);
        assertEquals(expected, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(builder.get()));
        assertEquals(expected + " The description", OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(builderWithDesc.get()));
    }

    @Test
    void testGetDescription() {
        final Option normalOption = Option.builder().option("o").longOpt("one").hasArg().desc("The description").get();
        final Option deprecatedOption = Option.builder().option("o").longOpt("one").hasArg().desc("The description").deprecated().get();
        final Option deprecatedOptionWithAttributes = Option.builder().option("o").longOpt("one").hasArg().desc("The description").deprecated(DeprecatedAttributes.builder().setForRemoval(true).setSince("now").setDescription("Use something else").get()).get();
        assertEquals("The description", OptionFormatter.from(normalOption).getDescription(), "normal option failure");
        assertEquals("The description", OptionFormatter.from(deprecatedOption).getDescription(), "deprecated option failure");
        assertEquals("The description", OptionFormatter.from(deprecatedOptionWithAttributes).getDescription(), "complex deprecated option failure");
        OptionFormatter.Builder builder = OptionFormatter.builder().setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT);
        assertEquals("The description", builder.build(normalOption).getDescription(), "normal option failure");
        assertEquals("[Deprecated] The description", builder.build(deprecatedOption).getDescription(), "deprecated option failure");
        assertEquals("[Deprecated] The description", builder.build(deprecatedOptionWithAttributes).getDescription(), "complex deprecated option failure");
        builder = OptionFormatter.builder().setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);
        assertEquals("The description", builder.build(normalOption).getDescription(), "normal option failure");
        assertEquals("[Deprecated] The description", builder.build(deprecatedOption).getDescription(), "deprecated option failure");
        assertEquals("[Deprecated for removal since now. Use something else] The description", builder.build(deprecatedOptionWithAttributes).getDescription(), "complex deprecated option failure");
    }
}
