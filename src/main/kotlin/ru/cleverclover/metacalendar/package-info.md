# Package ru.cleverclover.metacalendar

#####Meta calendar
Imagine you want to remember a birthday of a friend, 
which inevitably happens every year. 
You just need to store "the second of June" somewhere. 

Or your professional holiday is celebrated at "the first Friday in November",
and you don't want to miss it.  

The place to store such an unfixed pointers is a meta-calendar.

Think of it as of a regular calendar, but without a year sticked to it. 
There are 365/366 days in 12 months named as you expect.
There are a notion of a week and day of a week, 
but those ones do not stick with precise dates.


##### Day mark
You can peek a point on a meta calendar. Like
 - 1-st of January
 - 28 (29) of February
 - the last Wednesday of November
 - the second Monday of May
 
We support several formats, but this is pluggable part of the model.

You can put a note to the mark in case it means something special.

##### Period
You can also define a period, it's just an ordered couple of day marks  
 
##### Resolution to a precise date 
The most interesting part of the meta calendar is 
your ability to put a particular year in it (in context of a time zone, of cause), 
and thus make it a real calendar with real dates marked and noted.  

