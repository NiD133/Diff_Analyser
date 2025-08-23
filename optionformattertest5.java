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

public class OptionFormatterTestTest5 {

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
    void testCopyConstructor() {
        final Function<Option, String> depFunc = o -> "Ooo Deprecated";
        final BiFunction<OptionFormatter, Boolean, String> fmtFunc = (o, b) -> "Yep, it worked";
        // @formatter:off
        final OptionFormatter.Builder builder = OptionFormatter.builder().setLongOptPrefix("l").setOptPrefix("s").setArgumentNameDelimiters("{", "}").setDefaultArgName("Some Argument").setOptSeparator(" and ").setOptionalDelimiters("?>", "<?").setSyntaxFormatFunction(fmtFunc).setDeprecatedFormatFunction(depFunc);
        // @formatter:on
        Option option = Option.builder("o").longOpt("opt").get();
        OptionFormatter formatter = builder.build(option);
        OptionFormatter.Builder builder2 = new OptionFormatter.Builder(formatter);
        assertEquivalent(formatter, builder2.build(option));
        option = Option.builder("o").longOpt("opt").deprecated().required().get();
        formatter = builder.build(option);
        builder2 = new OptionFormatter.Builder(formatter);
        assertEquivalent(formatter, builder2.build(option));
    }
}
