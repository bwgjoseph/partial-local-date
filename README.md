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

---

There is no need to explicitly create a custom deserializer from `request JSON` to `PartialLocalDate` because it is supported by default.

So imagine the request like such

```json
{
    "companyName": "SG",
    "localDate": "2022-08-16",
    "localDateTime": "2022-08-16T16:28:24.441",
    "offsetDateTime": "2022-08-16T16:28:24.441+07:00",
    "partialLocalDate": "2022-08-00"
}
```

Making a API call, and the serialized object will look like

```json
{
    "id": "62fbbbbbf036556e8d15bf1e",
    "companyName": "SG",
    "localDate": "2022-08-16",
    "localDateTime": "2022-08-16T16:28:24.441",
    "offsetDateTime": "2022-08-16T09:28:24.441Z",
    "partialLocalDate": {
        "temporalAccessor": "2022-08",
        "classifier": "YEAR_MONTH",
        "monthValue": 8,
        "yearValue": 2022,
        "dayValue": 0,
        "validLocalDate": false
    }
}
```

By default, `jackson` will use the `public field and getters` as part of the serialization process which is why we see it returns with an object and the various fields. However, what we actually need to return back to client is just a `String` like `2022-08-00`.

In this case, we can use `@JsonComponent` provided by `Spring` to write the custom serializer - [PartialLocalDateSerializer](src/main/java/com/bwgjoseph/partiallocaldate/PartialLocalDateSerializer.java) to return only the value we want. See [PartialLocalDateSerializerJsonTests](src/test/java/com/bwgjoseph/partiallocaldate/PartialLocalDateSerializerJsonTests.java)

Once implemented, the returned value from the API will become this

```json
{
    "id": "62fbbdddf036556e8d15bf21",
    "companyName": "SG",
    "localDate": "2022-08-16",
    "localDateTime": "2022-08-16T16:28:24.441",
    "offsetDateTime": "2022-08-16T09:28:24.441Z",
    "partialLocalDate": "2022-08-00"
}
```

Also see [EmploymentControllerTests](src/test/java/com/bwgjoseph/partiallocaldate/EmploymentControllerTests.java)

### Database Layer

Similarly, for `MongoDB`, we can use [MappingMongoConverter](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mapping-configuration) to map specific classes to and from the database

---

When we read and write from `MongoDB`, we can also specify how to "serialize" and "deserialize" `PartialLocalDate`. To do so, we can make use of [Spring Converter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/Converter.html) interface, and register those converters to [MongoCustomConversions](https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/core/convert/MongoCustomConversions.html) and it will handle the mapping automatically.

See [MongoPartialLocalDateReader](src/main/java/com/bwgjoseph/partiallocaldate/MongoPartialLocalDateReader.java), [MongoPartialLocalDateWriter](src/main/java/com/bwgjoseph/partiallocaldate/MongoPartialLocalDateWriter.java) and [MongoConfig](src/main/java/com/bwgjoseph/partiallocaldate/MongoConfig.java) for the implementation details.

The object that is saved in database would result in

```json
{
    _id: ObjectId('62fbbbbbf036556e8d15bf1e'),
    companyName: 'SG',
    localDate: ISODate('2022-08-15T16:00:00.000Z'),
    localDateTime: ISODate('2022-08-16T08:28:24.441Z'),
    offsetDateTime: {
        dateTime: ISODate('2022-08-16T09:28:24.441Z'),
        offset: 'Z'
    },
    partialLocalDate: {
        date: '2022-08-00',
        classifier: 'YEAR_MONTH',
        year: 2022,
        month: 8,
        day: 0
    },
    _class: 'com.bwgjoseph.partiallocaldate.EmploymentDO'
}
```

And when retrieved, it will be mapped back to `PartialLocalDate` object

## Test

See [PartialLocalDateTests](/src/test/java/com/bwgjoseph/partiallocaldate/PartialLocalDateTests.java), [PartialLocalDateSerializerJsonTests](src/test/java/com/bwgjoseph/partiallocaldate/PartialLocalDateSerializerJsonTests.java), [EmploymentControllerTests](src/test/java/com/bwgjoseph/partiallocaldate/EmploymentControllerTests.java), [EmploymentCrudRepositoryTests](src/test/java/com/bwgjoseph/partiallocaldate/EmploymentCrudRepositoryTests.java)