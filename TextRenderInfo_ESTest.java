import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.MarkedContentInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

@DisplayName("TextRenderInfo Tests")
class TextRenderInfoTest {

    private GraphicsState defaultGraphicsState;
    private Matrix identityMatrix;

    @BeforeEach
    void setUp() {
        defaultGraphicsState = createDefaultGraphicsState();
        identityMatrix = new Matrix();
    }

    /**
     * Creates a default GraphicsState with a "dummy" font.
     * The original tests use a CMapAwareDocumentFont created from an empty dictionary,
     * which results in a font with no metrics (ascent/descent are 0).
     * We replicate this as it's a valid edge case to test.
     */
    private GraphicsState createDefaultGraphicsState() {
        GraphicsState gs = new GraphicsState();
        gs.font = new CMapAwareDocumentFont(new PdfGState());
        gs.fontSize = 12; // A typical font size
        gs.horizontalScaling = 100; // Default scaling
        return gs;
    }

    /**
     * Helper to create a TextRenderInfo instance with common defaults.
     */
    private TextRenderInfo createTextRenderInfo(String text, GraphicsState gs, Matrix textMatrix) {
        PdfString pdfString = new PdfString(text);
        return new TextRenderInfo(pdfString, gs, textMatrix, new ArrayList<>());
    }

    @Nested
    @DisplayName("Simple Getters")
    class SimpleGettersTest {

        @Test
        void getText_returnsTheDecodedString() {
            TextRenderInfo info = createTextRenderInfo("Hello World", defaultGraphicsState, identityMatrix);
            assertThat(info.getText()).isEqualTo("Hello World");
        }

        @Test
        void getPdfString_returnsTheOriginalPdfString() {
            PdfString pdfString = new PdfString("Test");
            TextRenderInfo info = new TextRenderInfo(pdfString, defaultGraphicsState, identityMatrix, Collections.emptyList());
            assertThat(info.getPdfString()).isSameAs(pdfString);
        }

        @Test
        void getFont_returnsFontFromGraphicsState() {
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, identityMatrix);
            assertThat(info.getFont()).isSameAs(defaultGraphicsState.font);
        }

        @Test
        void getFillColor_returnsFillColorFromGraphicsState() {
            defaultGraphicsState.fillColor = BaseColor.RED;
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, identityMatrix);
            assertThat(info.getFillColor()).isEqualTo(BaseColor.RED);
        }

        @Test
        void getStrokeColor_returnsStrokeColorFromGraphicsState() {
            defaultGraphicsState.strokeColor = BaseColor.BLUE;
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, identityMatrix);
            assertThat(info.getStrokeColor()).isEqualTo(BaseColor.BLUE);
        }

        @Test
        void getTextRenderMode_returnsModeFromGraphicsState() {
            defaultGraphicsState.renderMode = 2; // Fill, then stroke
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, identityMatrix);
            assertThat(info.getTextRenderMode()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Geometric Properties")
    class GeometricPropertiesTest {

        @Test
        void getBaseline_withDefaultState_returnsLineFromOriginAlongXAxis() {
            TextRenderInfo info = createTextRenderInfo("A", defaultGraphicsState, identityMatrix);
            float expectedWidth = info.getUnscaledWidth();

            LineSegment baseline = info.getBaseline();

            assertThat(baseline.getStartPoint()).isEqualTo(new Vector(0, 0, 1));
            assertThat(baseline.getEndPoint().get(0)).isCloseTo(expectedWidth, within(0.001f));
            assertThat(baseline.getEndPoint().get(1)).isCloseTo(0f, within(0.001f));
        }

        @Test
        void getBaseline_whenTransformed_returnsTransformedLine() {
            Matrix transform = new Matrix(2, 0, 0, 3, 10, 20); // Scale and translate
            TextRenderInfo info = createTextRenderInfo("A", defaultGraphicsState, transform);
            float expectedWidth = info.getUnscaledWidth() * 2; // Scaled width

            LineSegment baseline = info.getBaseline();

            assertThat(baseline.getStartPoint()).isEqualTo(new Vector(10, 20, 1));
            assertThat(baseline.getEndPoint().get(0)).isCloseTo(10 + expectedWidth, within(0.001f));
            assertThat(baseline.getEndPoint().get(1)).isCloseTo(20f, within(0.001f));
        }

        @Test
        void getAscentAndDescentLine_withDummyFont_areSameAsBaseline() {
            // The dummy font used in setup has ascent/descent of 0.
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, identityMatrix);

            LineSegment baseline = info.getBaseline();
            LineSegment ascentLine = info.getAscentLine();
            LineSegment descentLine = info.getDescentLine();

            assertThat(ascentLine).isEqualTo(baseline);
            assertThat(descentLine).isEqualTo(baseline);
        }

        @Test
        void getBaseline_whenTextHasRise_isShiftedVertically() {
            defaultGraphicsState.rise = 50f;
            TextRenderInfo info = createTextRenderInfo("A", defaultGraphicsState, identityMatrix);
            float expectedWidth = info.getUnscaledWidth();

            LineSegment baseline = info.getBaseline();

            assertThat(baseline.getStartPoint()).isEqualTo(new Vector(0, 50, 1));
            assertThat(baseline.getEndPoint().get(0)).isCloseTo(expectedWidth, within(0.001f));
            assertThat(baseline.getEndPoint().get(1)).isCloseTo(50f, within(0.001f));
        }

        @Test
        void getRise_whenRiseIsPositive_returnsScaledRise() {
            defaultGraphicsState.rise = 50f;
            Matrix transform = new Matrix().scale(1, 2); // Vertical scale of 2
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, transform);

            // Rise is scaled by the transformation matrix's Y-component
            assertThat(info.getRise()).isCloseTo(100f, within(0.001f));
        }

        @Test
        void getRise_whenRiseIsNegative_returnsScaledNegativeRise() {
            defaultGraphicsState.rise = -50f;
            Matrix transform = new Matrix().scale(1, 2); // Vertical scale of 2
            TextRenderInfo info = createTextRenderInfo("Test", defaultGraphicsState, transform);

            // The original EvoSuite test had a flawed assertion here. The result should be negative.
            assertThat(info.getRise()).isCloseTo(-100f, within(0.001f));
        }
    }

    @Nested
    @DisplayName("Width Calculations")
    class WidthCalculationTest {

        @Test
        void getUnscaledWidth_withCharacterSpacing_isWider() {
            TextRenderInfo infoWithoutSpacing = createTextRenderInfo("AB", defaultGraphicsState, identityMatrix);
            float widthWithoutSpacing = infoWithoutSpacing.getUnscaledWidth();

            defaultGraphicsState.characterSpacing = 10f;
            TextRenderInfo infoWithSpacing = createTextRenderInfo("AB", defaultGraphicsState, identityMatrix);
            float widthWithSpacing = infoWithSpacing.getUnscaledWidth();

            // Width should increase by (charCount - 1) * charSpacing
            assertThat(widthWithSpacing).isCloseTo(widthWithoutSpacing + 10f, within(0.001f));
        }

        @Test
        void getSingleSpaceWidth_withWordSpacing_isWider() {
            TextRenderInfo infoWithoutSpacing = createTextRenderInfo(" ", defaultGraphicsState, identityMatrix);
            float spaceWidth = infoWithoutSpacing.getSingleSpaceWidth();

            defaultGraphicsState.wordSpacing = 20f;
            TextRenderInfo infoWithSpacing = createTextRenderInfo(" ", defaultGraphicsState, identityMatrix);
            float spaceWidthWithWordSpacing = infoWithSpacing.getSingleSpaceWidth();

            // Single space width should increase by the word spacing
            assertThat(spaceWidthWithWordSpacing).isCloseTo(spaceWidth + 20f, within(0.001f));
        }

        @Test
        void getSingleSpaceWidth_withZeroHorizontalScaling_isZero() {
            defaultGraphicsState.horizontalScaling = 0f;
            TextRenderInfo info = createTextRenderInfo(" ", defaultGraphicsState, identityMatrix);
            assertThat(info.getSingleSpaceWidth()).isCloseTo(0f, within(0.001f));
        }

        @Test
        void getSingleSpaceWidth_withNegativeFontSize_returnsNegativeWidth() {
            defaultGraphicsState.fontSize = -12f;
            TextRenderInfo info = createTextRenderInfo(" ", defaultGraphicsState, identityMatrix);
            
            // The original EvoSuite test incorrectly expected a positive value.
            // The underlying calculation (BaseFont.getWidthPoint) produces a negative width.
            assertThat(info.getSingleSpaceWidth()).isLessThan(0);
        }
    }

    @Nested
    @DisplayName("Character-Level Information")
    class CharacterLevelInfoTest {

        @Test
        void getCharacterRenderInfos_forSimpleString_returnsOneInfoPerCharacter() {
            String text = "Hello";
            TextRenderInfo info = createTextRenderInfo(text, defaultGraphicsState, identityMatrix);

            List<TextRenderInfo> charInfos = info.getCharacterRenderInfos();

            assertThat(charInfos).hasSize(text.length());
            assertThat(charInfos.get(0).getText()).isEqualTo("H");
            assertThat(charInfos.get(4).getText()).isEqualTo("o");
        }

        @Test
        void getCharacterRenderInfos_withSpacing_positionsCharactersCorrectly() {
            String text = "AB";
            defaultGraphicsState.characterSpacing = 10f;
            TextRenderInfo info = createTextRenderInfo(text, defaultGraphicsState, identityMatrix);

            List<TextRenderInfo> charInfos = info.getCharacterRenderInfos();
            TextRenderInfo charAInfo = charInfos.get(0);
            TextRenderInfo charBInfo = charInfos.get(1);

            float widthOfA = charAInfo.getUnscaledWidth();
            float expectedB_start_x = widthOfA + defaultGraphicsState.characterSpacing;

            // The start position of 'B' is encoded in its transformation matrix
            Vector startOfB = new Vector(0, 0, 1).cross(charBInfo.getUnscaledBaseline().getStartPoint().getMatrix());
            
            assertThat(startOfB.get(0)).isCloseTo(expectedB_start_x, within(0.01f));
        }
    }

    @Nested
    @DisplayName("Marked Content")
    class MarkedContentTest {
        @Test
        void hasMcid_whenIdIsPresent_returnsTrue() {
            MarkedContentInfo mcInfo = new MarkedContentInfo(PdfName.P, null, 123);
            TextRenderInfo info = new TextRenderInfo(new PdfString("test"), defaultGraphicsState, identityMatrix, Collections.singletonList(mcInfo));

            assertThat(info.hasMcid(123)).isTrue();
        }

        @Test
        void hasMcid_whenIdIsNotPresent_returnsFalse() {
            MarkedContentInfo mcInfo = new MarkedContentInfo(PdfName.P, null, 123);
            TextRenderInfo info = new TextRenderInfo(new PdfString("test"), defaultGraphicsState, identityMatrix, Collections.singletonList(mcInfo));

            assertThat(info.hasMcid(999)).isFalse();
        }

        @Test
        void getMcid_whenInMarkedContent_returnsIdFromTop() {
            MarkedContentInfo mcInfo = new MarkedContentInfo(PdfName.P, null, 42);
            TextRenderInfo info = new TextRenderInfo(new PdfString("test"), defaultGraphicsState, identityMatrix, Collections.singletonList(mcInfo));

            assertThat(info.getMcid()).isEqualTo(42);
        }

        @Test
        void getMcid_whenNoMarkedContent_returnsNull() {
            TextRenderInfo info = createTextRenderInfo("test", defaultGraphicsState, identityMatrix);
            assertThat(info.getMcid()).isNull();
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandlingTest {

        @Test
        void constructor_withNullMarkedContent_throwsNullPointerException() {
            assertThatThrownBy(() -> new TextRenderInfo(new PdfString(""), defaultGraphicsState, identityMatrix, null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void hasMcid_withNullInContentStack_throwsNullPointerException() {
            List<MarkedContentInfo> contentInfos = new ArrayList<>();
            contentInfos.add(null);
            TextRenderInfo info = new TextRenderInfo(new PdfString(""), defaultGraphicsState, identityMatrix, contentInfos);

            assertThatThrownBy(() -> info.hasMcid(1))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void methods_withInvalidCharsetInPdfString_throwExceptions() {
            // Test for UnsupportedCharsetException
            PdfString unsupportedString = new PdfString("test", "Invalid-Charset-Name");
            TextRenderInfo unsupportedInfo = new TextRenderInfo(unsupportedString, defaultGraphicsState, identityMatrix, Collections.emptyList());
            assertThatThrownBy(unsupportedInfo::getText).isInstanceOf(UnsupportedCharsetException.class);

            // Test for IllegalCharsetNameException
            PdfString illegalString = new PdfString("test", "??invalid??");
            TextRenderInfo illegalInfo = new TextRenderInfo(illegalString, defaultGraphicsState, identityMatrix, Collections.emptyList());
            assertThatThrownBy(illegalInfo::getText).isInstanceOf(IllegalCharsetNameException.class);
        }

        @Test
        void methods_withNullPdfString_throwNullPointerException() {
            TextRenderInfo info = new TextRenderInfo(null, defaultGraphicsState, identityMatrix, Collections.emptyList());

            assertThatThrownBy(info::getText).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(info::getUnscaledWidth).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(info::getCharacterRenderInfos).isInstanceOf(NullPointerException.class);
        }
    }
}