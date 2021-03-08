# OpenDemo
This repository is a technical test on the correct use of an Android architecture, with third-party libraries, the Jetpack package and the use of SOLID principles. The Marvel API has been used, with which a list with the Characters is painted, and the details of them are accessed. In addition, a Favorites section has been added, where they can be added from the detail screen of a Character.It is just an example, but it could be used as a base for any app that needs the use of an MVVM design pattern, as well as the libraries and tools that are included here, these are:

    Kotlin
    AndroidX
        App Compat
        RecyclerView
    Material Components
    Android Architecture Components
        Lifecycle and ViewModel
    Retrofit
    Kotlin Coroutines
    Koin
    Coil
    Test: jUnit
    Navigation
    BottomNavigationView & FloatingButton
    Room
    SwipeRefresh
    
# Project Structure

This project is built using Clean Architecture and is structured in the following way:

 - app module - contains Activities/Fragments and their corresponding ViewModels and Adapters for the presentation layer. Also incluso a class for di with koin for the ViewModels & the Application class.

 - core module - contains entities and use cases for the presentation layer to access data from the data layer, contains data models and repositories for getting data,ontains implementation details for network and db layer (Retrofit/Room),contains classes for dependency injection(in this case Koin).

# Marvel API

The API keys should be in a gradle.properties in the root folder and will be loaded into the app as BuildConfig fields.The fields here are PUBLIC_API_KEY & PRIVATE_API_KEY. You need add here yours keys for access to the API Marvel.

You can create your own API keys on [Marvel's developer site] (https://developer.marvel.com/documentation/getting_started)

The public api key is in gradle.properties and the hash is created in the RemoteModule, in the request builder. Read more about their API Authorization here.

# Resources

Some links of interest to make this app have been:

- Clean Architecture:
   * https://antonioleiva.com/clean-architecture-android
   * https://proandroiddev.com/kotlin-clean-architecture-1ad42fcd97fa

- Koin:
  * https://insert-koin.io/
  * https://medium.com/droid-latam/c%C3%B3mo-configurar-koin-como-framework-de-inyecci%C3%B3n-de-dependencias-di-a-un-proyecto-android-sin-90f9e2ad961e

- Modularization:
  * https://medium.com/mindorks/multiple-application-modules-in-one-android-project-36e86ceb8a9
  * https://medium.com/google-developer-experts/modularizing-android-applications-9e2d18f244a0

- Coroutines:
  * https://developer.android.com/kotlin/coroutines
  * https://kotlinlang.org/docs/coroutines-basic-jvm.html

- Android Ptterns:
  * https://blog.mindorks.com/mastering-design-patterns-in-android-with-kotlin
  * https://devexperto.com/mvvm-vs-mvp/

# Authors

Manuel Blanco Murillo - Android Developer - mblancomu@gmail.com

# License

Copyright 2021 Manuel Blanco Murillo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at


    http://www.apache.org/licenses/LICENSE-2.0


Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and
limitations under the License.

   
