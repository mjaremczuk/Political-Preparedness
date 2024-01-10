## Political Preparedness

PolitcalPreparedness is an example application built to demonstrate core Android Development skills as presented in the Udacity Android Developers Kotlin curriculum.
Application uses https://www.googleapis.com/civicinfo/ api for demo purposes only. None of government institutions is using application to provide information

This app demonstrates the following views and techniques:

* [Retrofit](https://square.github.io/retrofit/) to make api calls to an HTTP web service.
* [Moshi](https://github.com/square/moshi) which handles the deserialization of the returned JSON to Kotlin data objects. 
* [Glide](https://bumptech.github.io/glide/) to load and cache images by URL.
* [Room](https://developer.android.com/training/data-storage/room) for local database storage.
  
It leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding adapters
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) with the SafeArgs plugin for parameter passing between fragments


## Setting up the Repository

To get started with this project, simply pull the repository and import the project into Android Studio. From there, deploy the project to an emulator or device. 

* NOTE: In order for this project to pull data, you will need to add your API Key to the project as a value in the CivicsHttpClient. You can generate an API Key from the [Google Developers Console](https://console.developers.google.com/)

## Getting Started

* For the most part, the TODOs in the project will guide you through getting the project completed. There is a general package architecture and *most* files are present. 
* Hints are provided for tricky parts of the application that may extend beyond basic Android development skills.
* As databinding is integral to the project architecture, it is important to be familiar with the IDE features such s cleaning and rebuilding the project as well as invalidating caches. 

## Suggested Workflow

* It is recommend you save all beautification until the end of the project. Ensure functionality first, then clean up UI. While UI is a component of the application, it is best to deliver a functional product.
* Start by getting all screens in the application to navigate to each other, even with dummy data. If needed, comment out stub code to get the application to compile. You will need to create actions in *nav_graph.xml* and UI elements to trigger the navigation. 
* Create an API key and begin work on the Elections Fragment  and associated ViewModel. 
	* Use the elections endpoint in the Civics API and requires no parameters.
	* You will need to create a file to complete the step.
	* This will require edits to the Manifest.
	* Link the election to the Voter Info Fragment.
* Begin work on the Voter Info Fragment and associated ViewModel.
* Begin work on the Representative Fragment and associated ViewModel.
	* This will require edits to Gradle.
	* You will need to create a file to complete the step.


### Dependencies

```
 // Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version_kotlin"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version_kotlin_coroutines"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version_kotlin_coroutines"
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$version_kotlin_serialization"
    implementation "androidx.test.espresso:espresso-idling-resource:$version_espresso"

    // Constraint Layout
    implementation "androidx.constraintlayout:constraintlayout:$version_constraint_layout"

    // ViewModel and LiveData
    implementation "androidx.lifecycle:lifecycle-extensions:$version_lifecycle_extensions"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$version_lifecycle_extensions"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$version_lifecycle_extensions"

    // Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:$version_navigation"
    implementation "androidx.navigation:navigation-ui-ktx:$version_navigation"

    // Core with Ktx
    implementation "androidx.core:core-ktx:$version_core"

    implementation "org.koin:koin-android:$version_koin"
    implementation "org.koin:koin-androidx-viewmodel:$version_koin"

    // Retrofit
    implementation "com.squareup.retrofit2:retrofit: $version_retrofit"
    implementation "com.squareup.retrofit2:converter-moshi:$version_retrofit"
    implementation "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:$version_retrofit_coroutines_adapter"
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Moshi
    implementation "com.squareup.moshi:moshi:$version_moshi"
    implementation "com.squareup.moshi:moshi-kotlin:$version_moshi"
    implementation "com.squareup.moshi:moshi-adapters:$version_moshi"

    // Picasso
    implementation 'com.squareup.picasso:picasso:2.5.2'

    //Room
    implementation "androidx.room:room-runtime:$version_room"
    implementation "androidx.room:room-ktx:$version_room"
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0'
    kapt "androidx.room:room-compiler:$version_room"

    // Location
    implementation "com.google.android.gms:play-services-location:$version_play_services_location"

    // Dependencies for local unit tests
    testImplementation "junit:junit:$version_junit"
    testImplementation "org.hamcrest:hamcrest-all:$version_hamcrest"
    testImplementation "androidx.arch.core:core-testing:$version_arch_testing"
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version_kotlin_coroutines"
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version_kotlin_coroutines"
    testImplementation "org.robolectric:robolectric:$version_robolectric"
    testImplementation "com.google.truth:truth:$version_truth"
    testImplementation "org.mockito:mockito-core:$version_mockito"

    // AndroidX Test - JVM testing
    testImplementation "androidx.test:core-ktx:$version_androidx_test_core"
    testImplementation "androidx.test.ext:junit-ktx:$version_androidx_test_ext_kotlin_runner"
    testImplementation "androidx.test:rules:$version_androidx_test_rules"


    // AndroidX Test - Instrumented testing
    androidTestImplementation "androidx.test:core-ktx:$version_androidx_test_core"
    androidTestImplementation "androidx.test.ext:junit-ktx:$version_androidx_test_ext_kotlin_runner"
    androidTestImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version_kotlin_coroutines"
    androidTestImplementation "androidx.test:rules:$version_androidx_test_rules"
    androidTestImplementation "androidx.room:room-testing:$version_room"
    androidTestImplementation "androidx.arch.core:core-testing:$version_arch_testing"
    androidTestImplementation "org.robolectric:annotations:$version_robolectric"
    androidTestImplementation "androidx.test.espresso:espresso-core:$version_espresso"
    androidTestImplementation "androidx.test.espresso:espresso-contrib:$version_espresso"
    androidTestImplementation "androidx.test.espresso:espresso-intents:$version_espresso"
    androidTestImplementation "androidx.test.espresso.idling:idling-concurrent:$version_espresso"
    androidTestImplementation "junit:junit:$version_junit"

    androidTestImplementation "org.mockito:mockito-core:$version_mockito"
    androidTestImplementation "com.linkedin.dexmaker:dexmaker-mockito:$version_dex_maker"
    androidTestImplementation('org.koin:koin-test:2.0.1') { exclude group: 'org.mockito' }
    androidTestImplementation 'com.azimolabs.conditionwatcher:conditionwatcher:0.2'

    implementation "androidx.fragment:fragment-testing:$version_fragment"
    implementation "androidx.test:core:$version_androidx_test_core"
    implementation "androidx.fragment:fragment-ktx:$version_fragment"
    implementation "androidx.activity:activity:$version_activity"
    implementation "androidx.activity:activity-ktx:$version_activity"

    androidTestImplementation 'androidx.test:runner:1.3.0'
    androidTestUtil 'androidx.test:orchestrator:1.3.0'
```

### Installation

Step by step explanation of how to get a dev environment running.

List out the steps

```
Given 	You cloned or downloaded repository
When 	Project is opened
Then 	Find or create file in project root folder: local.properties
And 	Add new line in file containing api key required to make requests to civics api: api_key=<YOUR_API_KEY_HERE>
And	Run the app 
```

## Testing

To run automated tests just use connectedCheck gradle task:

![image](https://user-images.githubusercontent.com/7701924/111063455-d0f7aa80-84ae-11eb-9ef3-03adf11d2bbe.png)

## Project Instructions

App screenshots:

![image](https://user-images.githubusercontent.com/7701924/111063773-deae2f80-84b0-11eb-92d6-c79f8ba6cd9a.png)
![image](https://user-images.githubusercontent.com/7701924/111063786-ea99f180-84b0-11eb-8ac3-712919ec881f.png)
![image](https://user-images.githubusercontent.com/7701924/111063802-f84f7700-84b0-11eb-8bd7-5681a0694b6c.png)
![image](https://user-images.githubusercontent.com/7701924/111063814-01404880-84b1-11eb-89ac-1fe9ec572467.png)
![image](https://user-images.githubusercontent.com/7701924/111063824-0c937400-84b1-11eb-8836-9ab0ef76a0b6.png)
![image](https://user-images.githubusercontent.com/7701924/111063833-14ebaf00-84b1-11eb-8011-092cf8bbc74d.png)
![image](https://user-images.githubusercontent.com/7701924/111063840-1fa64400-84b1-11eb-9424-fe0637b2677d.png)

Video:

https://user-images.githubusercontent.com/7701924/111064360-f89d4180-84b3-11eb-99ae-da8b916bab8d.mp4


## License

License available here: 
https://github.com/mjaremczuk/Political-Preparedness/blob/main/LICENSE.txt
