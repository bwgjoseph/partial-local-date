# Partial Local Date

A wrapper class around valid or partial date in `yyyy-MM-dd` format such as

```
2022-01-01 [LocalDate]
2022-01-00 [YearMonth]
2022-00-01 [YearDay]
0000-01-01 [MonthDay]
2022-00-00 [Year]
0000-01-00 [Month]
0000-00-01 [Day]
0000-00-00 [Dateless]
```

## Usage

There are a few ways to construct `PartialLocalDate` instant

```java
// via constructor
PartialLocalDate pld = new PartialLocalDate("0000-10-30");

// via static method
PartialLocalDate pld = PartialLocalDate.of(0000);
PartialLocalDate pld = PartialLocalDate.of(0000, 10);
PartialLocalDate pld = PartialLocalDate.of(0000, 10, 30);
```

Calling `toString` will always return in `yyyy-MM-dd` format, no matter the type of partial date

```
0000-10-30
0000-00-00
2022-00-30
0000-00-30
```

It also provides a `Classifier` enum such that one would know what is the construct of the `PartialLocalDate`

```java
enum Classifier {
    LOCAL_DATE,
    YEAR_MONTH,
    MONTH_DAY,
    YEAR_DAY,
    YEAR,
    MONTH,
    DAY,
    DATELESS
}
```

It also provides methods to query for

- `Year, Month, or Day` value
- Whether if it's a valid Date (i.e `2022-08-04`)

## Implementation Details

The core idea is to make use of `TemporalAccessor` which is a `base interface type for date, time and offset objects` to represent the `PartialLocalDate` internally.

However, one challenge is that there is no default support for `Day` (i.e `0000-00-10`) or `Dateless` (i.e `0000-00-00`). As such, I implemented a simple `Day` class which implements `TemporalAccessor` and for `Dateless`, we can classify it using a `Year` class.

## Integration

This can be further integrates at two layer

- HTTP Layer
- Database Layer

This will abstract away the need to manually handle the conversion, and also for developer to only work with `PartialLocalDate` rather than `String`

### HTTP Layer

We can use [@JsonComponent](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/jackson/JsonComponent.html) to register the custom serializer and deserializer from `String` to `PartialLocalDate` and vice versa

### Database Layer

Similarly, for `MongoDB`, we can use [MappingMongoConverter](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mapping-configuration) to map specific classes to and from the database

## Test

See [PartialLocalDateTests](/src/test/java/com/bwgjoseph/partiallocaldate/PartialLocalDateTests.java)