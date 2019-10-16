### meta-calendar

 [![Build Status](https://travis-ci.com/eparovyshnaya/meta-calendar.svg?branch=master)](https://travis-ci.com/eparovyshnaya/meta-calendar)
 [![Codacy Badge](https://api.codacy.com/project/badge/Grade/6d05c5201f9e4b0a90359399c570f13a)](https://www.codacy.com/manual/elena.parovyshnaya/meta-calendar?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eparovyshnaya/meta-calendar&amp;utm_campaign=Badge_Grade)
 [![codecov](https://codecov.io/gh/eparovyshnaya/meta-calendar/branch/master/graph/badge.svg)](https://codecov.io/gh/eparovyshnaya/meta-calendar)
 [![Hits-of-Code](https://hitsofcode.com/github/eparovyshnaya/meta-calendar)](https://hitsofcode.com/view/github/eparovyshnaya/meta-calendar)

[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/eparovyshnaya/meta-calendar/blob/master/LICENSE)

[![Release](https://img.shields.io/badge/Release-Latest%201.0.1-pink.svg)](https://github.com/eparovyshnaya/meta-calendar/releases/latest)

#### what is it all about
It's sort of a data structure with a plain API to handle time *period*s.
*Period*s are not nailed down to a particular dates, 
so you cannot settle an alarm clock to avoid missing start of such a *period*.
Instead, they are expressed freely like
 - autumn school holidays: from the last saturday of October till the first saturday of November
 - Judith's birthday: 21-st of August
 - Jol in Iceland: from 12-th of December till 25-th of December
 - time to let your hair down: the last day of December
 - you name it
 
#### what's the use 
Not much of it, actually. You can
 - **construct** these periods out of natural language phrases like the ones listed above, or by formal api
>by dsl (have a look at [DslTest](src/test/kotlin/ru/cleverclover/metacalendar/compose/DslTest.kt))
 ```kotlin
    calendar {
            period(
                    from = "третья среда ноября",
                    till = "17 января",
                    note = "dark time"
            )
    }
```
>by nl api (see [PeriodParseTest](src/test/kotlin/ru/cleverclover/metacalendar/parse/PeriodParseTest.kt))
 ```kotlin
    MetaCalendar().apply {
            addPeriod(PeriodFromRangeDefinition("с 1 января по 8 февраля")
                .bounds().period())
            addPeriod(PeriodFromBoundDefinitions("1 января", "8 июля")
                .bounds().period())
    }
```
>by regular api
```kotlin
MetaCalendar().apply {
    addPeriod(Period(DayOfMonth(Month.JANUARY, 1), 
                     LastDayOfMonth(Month.FEBRUARY)))
}
```
 - **resolve** any of'em (or every, in one fell swoop) to *real date periods*,
  having a precise *year* and *time zone* to get pairs of *java.time.ZonedDateTime* objects. 
  The ones you actually can set an alarm clock for.
  (see [CalendarResolutionTest](src/test/kotlin/ru/cleverclover/metacalendar/resolve/CalendarResolutionTest.kt))
```kotlin
MetaCalendar().apply { 
        addPeriod(Period(DayOfMonth(Month.JANUARY, 1), 
                         LastDayOfMonth(Month.FEBRUARY))) 
    }.resolve(2019)
```

#### how to plug

 - the library distributive resides under [jcenter](https://mvnrepository.com/repos/jcenter) maven repository.
 To add a repository to your, say, gradle build, use the dedicated gradle dsl function
```groovy
repositories {
    jcenter()
}    
```
or configure it directly 
```kotlin
repositories {
    maven(url = "https://jcenter.bintray.com")
}
```

 - latest released artifact verion can be found, let alone this place, at [bintray](https://bintray.com/eparovyshnaya/clever-clover/meta-calendar) presence page.
 Include the library in your, say, gradle build as follows (groovy dsl sample)
 ```groovy 
compile 'ru.clever-clover.meta-calendar:meta-calendar:1.0.1'
``` 

#### yet to be done
 - i18n is going to happen, currently ru-l10n is implanted [#6](https://github.com/eparovyshnaya/meta-calendar/issues/6)
 - dsl extension ability [#5](https://github.com/eparovyshnaya/meta-calendar/issues/5)

