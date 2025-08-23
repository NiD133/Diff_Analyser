package org.jsoup.select;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link QueryParser}.
 * This test focuses on how specific CSS query structures are parsed into Evaluator trees.
 */
public class QueryParserTest {

    @Test
    @DisplayName("A query with chained child combinators ('>') should be parsed into an optimized ImmediateParentRun evaluator")
    void queryWithChainedChildCombinators_ParsesTo_ImmediateParentRun() {
        // Arrange: A CSS query with a sequence of child combinators.
        // This structure is a candidate for a specific parsing optimization.
        String query = "div > p > bold.brass";

        // The expected S-expression representation of the parsed evaluator tree.
        // - ImmediateParentRun: An optimized evaluator for a > b > c chains.
        // - It contains the sequence of evaluators: Tag 'div', Tag 'p', and an And evaluator.
        // - The final part 'bold.brass' is an And of Tag 'bold' and Class '.brass'.
        String expectedStructure = "(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))";

        // Act: Parse the query and get its S-expression representation.
        String actualStructure = sexpr(query);

        // Assert: The actual parsed structure must match the expected optimized structure.
        assertEquals(expectedStructure, actualStructure);
    }
}