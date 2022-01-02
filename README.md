# Weather Logger:
An Android weather application implemented using the MVVM pattern,
Retrofit2,
Dagger Hilt,
LiveData,
ViewModel,
Coroutines,
Room,
Navigation Components,
Data Binding and some other libraries 


## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the to manage fragment operations.
* Pattern [Model-View-ViewModel](MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components] which help to keep the application robust, testable, and maintainable.

## Technologies used:

* [Retrofit](https://square.github.io/retrofit/) a REST Client for Android which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice.
* [Dagger Hilt](https://dagger.dev/hilt/) for dependency injection.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) to handle data in a lifecycle-aware fashion.
* [Navigation Component](https://developer.android.com/guide/navigation) to handle all navigations and also passing of data between destinations.
* [Timber](https://github.com/JakeWharton/timber) - a logger with a small, extensible API which provides utility on top of Android's normal Log class.
* [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager) to manage Android background jobs.
* [Material Design](https://material.io/develop/android/docs/getting-started/) an adaptable system of guidelines, components, and tools that support the best practices of user interface design.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) used to manage the local storage i.e. `writing to and reading from the database`. Coroutines help in managing background threads and reduces the need for callbacks.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) to declaratively bind UI components in layouts to data sources.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Android KTX](https://developer.android.com/kotlin/ktx) which helps to write more concise, idiomatic Kotlin code.
* [Preferences](https://developer.android.com/guide/topics/ui/settings) to create interactive settings screens.

## App Flow:
- save icon loads weather data from local Room if exists or load current from api depending on current location.
- swipe to refresh for force refresh weather data and save it local in room.
- search for different cities and show it's weather details.
- periodic work manager for weather update by city id which saved in shared perf.
