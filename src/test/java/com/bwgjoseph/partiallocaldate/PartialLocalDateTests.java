package com.bwgjoseph.partiallocaldate;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartialLocalDateTests {

    @DisplayName("Test init via Constructor")
    @ParameterizedTest(name = "{index} => inputDate={0}, year={1}, month={2}, day={3}, classifier={4}, isProperdate={5}")
    @MethodSource()
    void testConstructorInit(String inputDate, int year, int month, int day, PartialLocalDate.Classifier classifier, boolean isProperDate) {
        PartialLocalDate partialLocalDate = new PartialLocalDate(inputDate);

        Assertions.assertThat(partialLocalDate.getYearValue()).isEqualTo(year);
        Assertions.assertThat(partialLocalDate.getMonthValue()).isEqualTo(month);
        Assertions.assertThat(partialLocalDate.getDayValue()).isEqualTo(day);
        Assertions.assertThat(partialLocalDate.getClassifier()).isEqualTo(classifier);
        Assertions.assertThat(partialLocalDate.isValidLocalDate()).isEqualTo(isProperDate);
    }

    private static Stream<Arguments> testConstructorInit() {
        return Stream.of(
            Arguments.of("2022-10-30", 2022, 10, 30, PartialLocalDate.Classifier.LOCAL_DATE, true),
            Arguments.of("2022-10-00", 2022, 10, 00, PartialLocalDate.Classifier.YEAR_MONTH, false),
            Arguments.of("2022-00-30", 2022, 00, 30, PartialLocalDate.Classifier.YEAR_DAY, false),
            Arguments.of("0000-10-30", 0000, 10, 30, PartialLocalDate.Classifier.MONTH_DAY, false),
            Arguments.of("2022-00-00", 2022, 00, 00, PartialLocalDate.Classifier.YEAR, false),
            Arguments.of("0000-10-00", 0000, 10, 00, PartialLocalDate.Classifier.MONTH, false),
            Arguments.of("0000-00-30", 0000, 00, 30, PartialLocalDate.Classifier.DAY, false),
            Arguments.of("0000-00-00", 0000, 00, 00, PartialLocalDate.Classifier.DATELESS, false)
        );
    }

    @DisplayName("Test init via static of method")
    @ParameterizedTest(name = "{index} => year={0}, month={1}, day={2}, classifier={3}, isProperdate={4}")
    @MethodSource()
    void testStaticOfInit(int year, int month, int day, PartialLocalDate.Classifier classifier, boolean isProperDate) {
        PartialLocalDate partialLocalDate = PartialLocalDate.of(year, month, day);

        Assertions.assertThat(partialLocalDate.getYearValue()).isEqualTo(year);
        Assertions.assertThat(partialLocalDate.getMonthValue()).isEqualTo(month);
        Assertions.assertThat(partialLocalDate.getDayValue()).isEqualTo(day);
        Assertions.assertThat(partialLocalDate.getClassifier()).isEqualTo(classifier);
        Assertions.assertThat(partialLocalDate.isValidLocalDate()).isEqualTo(isProperDate);
    }

    private static Stream<Arguments> testStaticOfInit() {
        return Stream.of(
            Arguments.of(2022, 10, 30, PartialLocalDate.Classifier.LOCAL_DATE, true),
            Arguments.of(2022, 10, 00, PartialLocalDate.Classifier.YEAR_MONTH, false),
            Arguments.of(2022, 00, 30, PartialLocalDate.Classifier.YEAR_DAY, false),
            Arguments.of(0000, 10, 30, PartialLocalDate.Classifier.MONTH_DAY, false),
            Arguments.of(2022, 00, 00, PartialLocalDate.Classifier.YEAR, false),
            Arguments.of(0000, 10, 00, PartialLocalDate.Classifier.MONTH, false),
            Arguments.of(0000, 00, 30, PartialLocalDate.Classifier.DAY, false),
            Arguments.of(0000, 00, 00, PartialLocalDate.Classifier.DATELESS, false)
        );
    }


    @Test
    void testStaticOfYear() {
        PartialLocalDate partialLocalDate = PartialLocalDate.of(2022);

        Assertions.assertThat(partialLocalDate.getYearValue()).isEqualTo(2022);
        Assertions.assertThat(partialLocalDate.getMonthValue()).isEqualTo(00);
        Assertions.assertThat(partialLocalDate.getDayValue()).isEqualTo(00);
        Assertions.assertThat(partialLocalDate.getClassifier()).isEqualTo(PartialLocalDate.Classifier.YEAR);
        Assertions.assertThat(partialLocalDate.isValidLocalDate()).isFalse();
    }

    @Test
    void testStaticOfYearMonth() {
        PartialLocalDate partialLocalDate = PartialLocalDate.of(2022, 10);

        Assertions.assertThat(partialLocalDate.getYearValue()).isEqualTo(2022);
        Assertions.assertThat(partialLocalDate.getMonthValue()).isEqualTo(10);
        Assertions.assertThat(partialLocalDate.getDayValue()).isEqualTo(00);
        Assertions.assertThat(partialLocalDate.getClassifier()).isEqualTo(PartialLocalDate.Classifier.YEAR_MONTH);
        Assertions.assertThat(partialLocalDate.isValidLocalDate()).isFalse();
    }

    @Test
    void testStaticOfNow() {
        PartialLocalDate partialLocalDate = PartialLocalDate.now();

        Assertions.assertThat(partialLocalDate.getYearValue()).isEqualTo(2022);
        Assertions.assertThat(partialLocalDate.getMonthValue()).isEqualTo(8);
        Assertions.assertThat(partialLocalDate.getDayValue()).isEqualTo(04);
        Assertions.assertThat(partialLocalDate.getClassifier()).isEqualTo(PartialLocalDate.Classifier.LOCAL_DATE);
        Assertions.assertThat(partialLocalDate.isValidLocalDate()).isTrue();
    }

    @ParameterizedTest
    @MethodSource
    void testInvalidDateFormat(String invalidDateFormat) {
        Assertions.assertThatThrownBy(() -> new PartialLocalDate(invalidDateFormat))
            .isExactlyInstanceOf(UnsupportedOperationException.class)
            .hasMessage("Not a valid date format, ensure in yyyy-MM-dd format");
    }

    private static Stream<Arguments> testInvalidDateFormat() {
        return Stream.of(
            Arguments.of("abc"),
            Arguments.of("12345"),
            Arguments.of("20221030"),
            Arguments.of("2022-1030"),
            Arguments.of("202210-30"),
            Arguments.of("2022-101-3")
        );
    }
}
