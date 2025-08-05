import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A clear, maintainable test suite for {@link JsonIgnoreProperties.Value}.
 * It is organized by the functionality being tested: Factory Methods, Wither Methods,
 * Merging Logic, Property Filtering, and Object Contract (equals, hashCode, toString).
 */
@DisplayName("JsonIgnoreProperties.Value")
class JsonIgnorePropertiesValueTest {

    private static final Set<String> IGNORED_PROPS_1 = Set.of("a", "b");
    private static final Set<String> IGNORED_PROPS_2 = Set.of("b", "c");

    // ===================================================================================
    // Factory Methods
    // ===================================================================================

    @Test
    void empty_shouldReturnDefaultInstance() {
        // Arrange & Act
        JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.empty();

        // Assert
        assertThat(emptyValue.getIgnored()).isEmpty();
        assertThat(emptyValue.getIgnoreUnknown()).isFalse();
        assertThat(emptyValue.getAllowGetters()).isFalse();
        assertThat(emptyValue.getAllowSetters()).isFalse();
        assertThat(emptyValue.getMerge()).isTrue();
        assertThat(emptyValue).isSameAs(JsonIgnoreProperties.Value.EMPTY);
    }

    @Test
    void forIgnoredProperties_withStrings_shouldCreateValueWithIgnoredNames() {
        // Arrange & Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");

        // Assert
        assertThat(value.getIgnored()).containsExactlyInAnyOrder("a", "b");
        assertThat(value.getIgnoreUnknown()).isFalse();
        assertThat(value.getMerge()).isTrue();
    }

    @Test
    void forIgnoredProperties_withSet_shouldCreateValueWithIgnoredNames() {
        // Arrange & Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties(IGNORED_PROPS_1);

        // Assert
        assertThat(value.getIgnored()).isEqualTo(IGNORED_PROPS_1);
        assertThat(value.getIgnoreUnknown()).isFalse();
        assertThat(value.getMerge()).isTrue();
    }

    @Test
    void forIgnoreUnknown_shouldCreateValueWithCorrectFlag() {
        // Arrange & Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Assert
        assertThat(value.getIgnored()).isEmpty();
        assertThat(value.getIgnoreUnknown()).isTrue();
        assertThat(value.getMerge()).isTrue();
    }

    @Test
    void from_withAnnotation_shouldCreateValueWithMergeDisabled() {
        // Arrange
        JsonIgnoreProperties annotation = createAnnotation(new String[]{"prop"}, true, true, true);

        // Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.from(annotation);

        // Assert
        assertThat(value.getIgnored()).containsExactly("prop");
        assertThat(value.getIgnoreUnknown()).isTrue();
        assertThat(value.getAllowGetters()).isTrue();
        assertThat(value.getAllowSetters()).isTrue();
        assertThat(value.getMerge()).isFalse(); // `from()` factory sets merge to false
    }

    @Test
    void from_withNull_shouldReturnEmptyValue() {
        // Arrange & Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.from(null);

        // Assert
        assertThat(value).isSameAs(JsonIgnoreProperties.Value.EMPTY);
    }

    // ===================================================================================
    // Wither Methods (Immutability)
    // ===================================================================================

    @Test
    void with_methods_shouldReturnNewInstanceWhenStateChanges() {
        // Arrange
        JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.empty();

        // Act & Assert
        assertThat(original.withIgnored("a")).isNotSameAs(original);
        assertThat(original.withIgnoreUnknown()).isNotSameAs(original);
        assertThat(original.withAllowGetters()).isNotSameAs(original);
        assertThat(original.withAllowSetters()).isNotSameAs(original);
        assertThat(original.withoutMerge()).isNotSameAs(original);
    }

    @Test
    void with_methods_shouldReturnSameInstanceWhenStateIsUnchanged() {
        // Arrange
        JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, true, true, false);

        // Act & Assert
        assertThat(original.withIgnored(IGNORED_PROPS_1)).isSameAs(original);
        assertThat(original.withIgnoreUnknown()).isSameAs(original);
        assertThat(original.withAllowGetters()).isSameAs(original);
        assertThat(original.withAllowSetters()).isSameAs(original);
        assertThat(original.withoutMerge()).isSameAs(original);
    }

    @Test
    void without_methods_shouldReturnNewInstanceWhenStateChanges() {
        // Arrange
        JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, true, true, true);

        // Act & Assert
        assertThat(original.withoutIgnored()).isNotSameAs(original);
        assertThat(original.withoutIgnoreUnknown()).isNotSameAs(original);
        assertThat(original.withoutAllowGetters()).isNotSameAs(original);
        assertThat(original.withoutAllowSetters()).isNotSameAs(original);
    }

    // ===================================================================================
    // Merging Logic
    // ===================================================================================

    @Test
    void merge_shouldCombinePropertiesCorrectly() {
        // Arrange
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, false, false, true, true);
        JsonIgnoreProperties.Value override = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_2, true, true, false, true);

        // Act
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.merge(base, override);

        // Assert
        assertThat(merged.getIgnored()).containsExactlyInAnyOrder("a", "b", "c");
        assertThat(merged.getIgnoreUnknown()).isTrue(); // false || true -> true
        assertThat(merged.getAllowGetters()).isTrue();   // false || true -> true
        assertThat(merged.getAllowSetters()).isTrue();   // true || false -> true
    }

    @Test
    void merge_shouldReturnOverride_whenOverrideHasMergeDisabled() {
        // Arrange
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties("a");
        JsonIgnoreProperties.Value override = JsonIgnoreProperties.Value.forIgnoredProperties("b").withoutMerge();

        // Act
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.merge(base, override);

        // Assert
        assertThat(merged).isSameAs(override);
        assertThat(merged.getIgnored()).containsExactly("b");
    }

    @Test
    void mergeAll_shouldCombineAllProperties() {
        // Arrange
        JsonIgnoreProperties.Value v1 = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, false, true, false, true);
        JsonIgnoreProperties.Value v2 = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_2, true, false, false, true);
        JsonIgnoreProperties.Value v3 = JsonIgnoreProperties.Value.construct(Collections.emptySet(), false, false, true, true);

        // Act
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(v1, v2, v3);

        // Assert
        assertThat(merged.getIgnored()).containsExactlyInAnyOrder("a", "b", "c");
        assertThat(merged.getIgnoreUnknown()).isTrue();
        assertThat(merged.getAllowGetters()).isTrue();
        assertThat(merged.getAllowSetters()).isTrue();
    }

    @Test
    void withOverrides_shouldPrioritizeOverrideProperties() {
        // Arrange
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, false, false, false, true);
        JsonIgnoreProperties.Value override = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_2, true, true, true, false);

        // Act
        JsonIgnoreProperties.Value result = base.withOverrides(override);

        // Assert
        assertThat(result.getIgnored()).containsExactlyInAnyOrder("a", "b", "c"); // Union of ignored names
        assertThat(result.getIgnoreUnknown()).isTrue(); // Override's 'true' wins
        assertThat(result.getAllowGetters()).isTrue();   // Override's 'true' wins
        assertThat(result.getAllowSetters()).isTrue();   // Override's 'true' wins
        assertThat(result.getMerge()).isFalse();          // Override's 'merge' flag is used
    }

    // ===================================================================================
    // Ignored Property Filtering
    // ===================================================================================

    @Test
    void findIgnoredForSerialization_shouldReturnEmptySet_whenAllowGettersIsTrue() {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties("a").withAllowGetters();

        // Act
        Set<String> ignored = value.findIgnoredForSerialization();

        // Assert
        assertThat(ignored).isEmpty();
    }

    @Test
    void findIgnoredForSerialization_shouldReturnIgnoredNames_whenAllowGettersIsFalse() {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties("a").withoutAllowGetters();

        // Act
        Set<String> ignored = value.findIgnoredForSerialization();

        // Assert
        assertThat(ignored).containsExactly("a");
    }

    @Test
    void findIgnoredForDeserialization_shouldReturnEmptySet_whenAllowSettersIsTrue() {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties("a").withAllowSetters();

        // Act
        Set<String> ignored = value.findIgnoredForDeserialization();

        // Assert
        assertThat(ignored).isEmpty();
    }

    @Test
    void findIgnoredForDeserialization_shouldReturnIgnoredNames_whenAllowSettersIsFalse() {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties("a").withoutAllowSetters();

        // Act
        Set<String> ignored = value.findIgnoredForDeserialization();

        // Assert
        assertThat(ignored).containsExactly("a");
    }

    // ===================================================================================
    // Object Contract (equals, hashCode, toString)
    // ===================================================================================

    @Test
    void equals_shouldBeTrueForEquivalentObjects() {
        // Arrange
        JsonIgnoreProperties.Value value1 = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, false, true, false);
        JsonIgnoreProperties.Value value2 = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, false, true, false);

        // Assert
        assertThat(value1).isEqualTo(value2);
    }

    @Test
    void equals_shouldBeFalseForDifferentProperties() {
        // Arrange
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, true, true, true);

        // Assert
        assertThat(base).isNotEqualTo(JsonIgnoreProperties.Value.construct(IGNORED_PROPS_2, true, true, true, true));
        assertThat(base).isNotEqualTo(JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, false, true, true, true));
        assertThat(base).isNotEqualTo(JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, false, true, true));
        assertThat(base).isNotEqualTo(JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, true, false, true));
        assertThat(base).isNotEqualTo(JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, true, true, false));
    }

    @Test
    void hashCode_shouldBeSameForEquivalentObjects() {
        // Arrange
        JsonIgnoreProperties.Value value1 = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, false, true, false);
        JsonIgnoreProperties.Value value2 = JsonIgnoreProperties.Value.construct(IGNORED_PROPS_1, true, false, true, false);

        // Assert
        assertThat(value1.hashCode()).isEqualTo(value2.hashCode());
    }

    @Test
    void toString_shouldProvideUsefulRepresentation() {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.construct(Set.of("a"), true, true, false, true);

        // Act
        String stringRepresentation = value.toString();

        // Assert
        assertThat(stringRepresentation).isEqualTo("JsonIgnoreProperties.Value(ignored=[a],ignoreUnknown=true,allowGetters=true,allowSetters=false,merge=true)");
    }

    /**
     * Helper to create a mock-like instance of the {@link JsonIgnoreProperties} annotation
     * for testing the {@code from()} factory method.
     */
    private JsonIgnoreProperties createAnnotation(String[] value, boolean ignoreUnknown, boolean allowGetters, boolean allowSetters) {
        return new JsonIgnoreProperties() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonIgnoreProperties.class;
            }

            @Override
            public String[] value() {
                return value;
            }

            @Override
            public boolean ignoreUnknown() {
                return ignoreUnknown;
            }

            @Override
            public boolean allowGetters() {
                return allowGetters;
            }

            @Override
            public boolean allowSetters() {
                return allowSetters;
            }
        };
    }
}