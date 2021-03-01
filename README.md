## Political Preparedness

PolitcalPreparedness is an example application built to demonstrate core Android Development skills as presented in the Udacity Android Developers Kotlin curriculum. 

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



## Report Issues
Notice any issues with a repository? Please file a github issue in the repository.


# README Template

Below is a template provided for use when building your README file for students.

# Project Title

Project description goes here.

## Getting Started

Instructions for how to get a copy of the project running on your local machine.

### Dependencies

```
Examples here
```

### Installation

Step by step explanation of how to get a dev environment running.

List out the steps

```
Give an example here
```

## Testing

Explain the steps needed to run any automated tests

### Break Down Tests

Explain what each test does and why

```
Examples here
```
## Project Instructions

This section should contain all the student deliverables for this project.

## Built With

* [Item1](www.item1.com) - Description of item
* [Item2](www.item2.com) - Description of item
* [Item3](www.item3.com) - Description of item

Include all items used to build project.

## License