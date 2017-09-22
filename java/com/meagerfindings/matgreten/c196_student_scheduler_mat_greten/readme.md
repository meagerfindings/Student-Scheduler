familiar# MOBILE APPLICATIONS DEVELOPMENT

### Written portions of assignment
  5. written portions
  4. readme.md

## Introduction to Mat Greten's Student Scheduler
- Compatibility
  - This app was written targeting API 26: Android 8.0 (O), Build Tools Version 26.0.1.
    - Minimum Sdk Version: API 25: Android 7.1.1 (Nougat)
    - Target Sdk Version: API 26: Android 8.0 (O)
  - During the initial development and was tested on a physical LGE Nexus 5x running API 26, Android (O), developer release 2, however due to stability issues on the device, not the Student Scheduler app, this device was rolled back to API 25, 7.1.1.
- After this, testing was performed on a virtual Nexus 5x and physical LGE Nexus 5x, both running API 25 Android 7.1.1.
- Unsigned APK file is located at `/Student_scheduler_mat_greten/app-debug.apk`
- Signed APK file is located at `/Student_scheduler_mat_greten/app-release.apk`
- Storyboard is saved in the `/main/images/story_board.png`.


## References List:

```
Android Camera. (2017, August 15). Retrieved September 10, 2017, from https://www.tutorialspoint.com/android/android_camera.htm
Example provided groundwork for understanding and implementing the camera. This resource was suggested by WGU class mentors with the suggestion that "You can follow the tutorial without having to make too many changes (all minor)."
```

```
Android Pickers. (2016, October 11). Retrieved September 12, 2017, from https://developer.android.com/guide/topics/ui/controls/pickers.html
Android documentation of date picker and time pickers.
```

```
Annur, B. M. (2013, October 27). How to use "Share image using" sharing Intent to share images in android? Retrieved September 10, 2017, from https://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android
Provided example of sharing multiple images at once though an intent. Adapted and added to author's code in order to share note and photos associated with note to external application of user's choice.
```

```
Creating Content ProvidersEdit PagePage History. (n.d.). Retrieved September 1, 2017, from https://guides.codepath.com/android/Creating-Content-Providers#contract-classes
Provided understanding from which the data layer of this application was built on to allow querying of database throughout the application.
```

```
Eric, A. (2012, May 24). Android-er: Cancel alarm with a matching PendingIntent . Retrieved September 12, 2017, from http://android-er.blogspot.com/2012/05/cancel-alarm-with-matching.html
Provided example of working notification cancelling after examining documentation and multiple sources did not clear this matter up.
```

```
Kothari, U. (2015, January 28). How to save bitmap from ImageView to database sqlite? Retrieved September 10, 2017, from https://stackoverflow.com/a/28186390
After attempting to convert to multiple formats, Umang's example provided working solution for translating camera input into format acceptable for storage within a SQLite BLOB type field.
```

```
Kurniawan, B. (2015). Android Application Development: A Beginners Tutorial. Retrieved September 11, 2017, from http://mmlviewer.books24x7.com/book/id_81425/viewer.asp?bookid=81425&chunkid=0224012307
Utilized throughout project, but particularly in creating, handling, and scheduling notifications.
```

```
Regular Expressions: Predefined Character Classes. (2015). Retrieved September 18, 2017, from https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html
Provided clarity on which characters would be excluded and included by performing regex matching on input strings, ultimately allowing sanitization of user input.
```

```
Sharing Content with Intents. (n.d.). Retrieved September 9, 2017, from https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
Source provided insight into using a fileprovider.xml file to allow local storage for images.
```


### Competencies:
- 4026.1.1: Introduction to Mobile Application Development - The graduate explains mobile development, develops a simple mobile application using IDE, documents debugging the mobile application, and describes how to use an emulator.
- 4026.1.2: Activity Lifecycle - The graduate describes the Activity lifecycle in the mobile application, and creates and links an activity.
- 4026.1.3: User Interfaces and Handling User Input - The graduate creates a user interface and describes how to handle user input.
- 4026.1.4: Saving Data - The graduate explains ways to save data in a mobile application, and creates a database in a mobile application.
- 4026.1.5: Sharing Information - The graduate explains how to share information in mobile applications and creates a user-defined content provider.
- 4026.1.6: Supporting Different Devices - The graduate describes how to utilize the available hardware and services available in different devices.
- 4026.1.7: Deploying Mobile Application - The graduate describes mobile application deployment and prepares an application for deployment.

## Task 1: Mobile Application Development
### Introduction:

> As a competent mobile application developer, your understanding of mobile application structure and design will help you to develop applications to meet customer requirements. The following project to develop a student scheduler/student progress tracking application, will help you to apply these skills in a familiar, real-world scenario. This task will allow you to demonstrate your ability to apply the skills learned in the course.

> You will develop a multiple-screen mobile application for WGU students to track their terms, courses associated with each term, and assessment(s) associated with each course. The application will allow students to enter, edit, and delete term, course, and assessment data. It should provide summary and detailed views of courses for each term and provide alerts for upcoming performance and objective assessments. This application will use a SQLite database.

### Input
_Each of the following needs to be input into the application:_

- terms, including the following:
  - the term title (e.g., Term 1, Term 2)
  - the start date
  - the end datetake)
  - the course mentor name(s), phone number(s), and email address(es)
  - objective and performance assessments associated with each course
  - the ability to add optional notes for each course
  - the ability to set alerts or notifications for the start and end date of each course
  -  the ability to set alerts for goal dates for each objective and performance assessment
  - the ability to insert photos in notes from a camera

### Output
_Each of the following needs to be displayed by the application on a separate screen:_
- the navigation panel
- a list of terms
- a detailed view of each term, including the following:
    - the title (e.g., Term 1, Term 2)
    - the start date
    - the end date
    - a list of courses for each term
- a detailed view of each course, including the following:
    - the course title
    - the start date
    - the anticipated end date
- assessments
    - a list of performance and objective assessments for a selected course
    - a detailed view of each objective and performance assessment, including the following:
    - the due date for a selected course
    - notes for the selected course
    - sharing features (e.g., email, SMS)

## Requirements:

_Note: Submit your performance assessment by including all Android project files, APK, and signed apk files in one zipped.zip file._

- All project files generated while completing this project can be found within `/student_scheduler_mat_greten/Student_scheduler_mat_greten`

- Unsigned APK file is located at `/Student_scheduler_mat_greten/app-debug.apk`
- Signed APK file is located at `/Student_scheduler_mat_greten/app-release.apk`


**Note**: For your convenience an optional checklist is attached to help guide you through this performance assessment.

### Checklist

#### A. Create an Android (version 4.0 or higher) mobile application with the following functional requirements:

1. Include the following information for each term:
  - the term title (e.g., Term 1, Term 2)
  - the start date
  - the end date
2. Include features that allow the user to add as many terms as needed.
3. Implement validation so that a term cannot be deleted if courses are assigned to it.
4. Include features that allow the user to do the following for each term:
  - add as many courses as needed
  - display a list of courses associated with each term
  - display a detailed view of each term, which includes the following information:
      - the term title (e.g., Term 1, Term 2)
      - the start date
      - the end date
5. Include the following details for each course:
  - the course title
  - the start date
  - the anticipated end date
  - the status (e.g., in progress, completed, dropped, plan to take)
  - the course mentor name(s), phone number(s), and e-mail address(es)
6. Include features that allow the user to do the following for each course:
  - add as many assessments as needed
  - add optional notes
  - enter, edit, and delete course information
  - display optional notes
  - display a detailed view of the course, including the due date
  - set alerts for the start and end date
  - share notes via a sharing feature (e.g., e-mail, SMS)
7. Include features that allow the user to do the following for each assessment:
  - add performance and/or objective assessments for each course, including the titles and due dates of the assessments
  - insert photos to notes using a camera
  - enter, edit, and delete assessment information
  - set alerts for goal dates

#### B. Design at least the following seven screen layouts, including appropriate GUI elements for each screen:
  - a home screen
  - a list of terms
  - a list of courses
  - a list of assessments
  - a detailed course view
  - a detailed term view
  - a detailed assessment view

Note: You are required to design the seven screens listed in part B but are not limited to only seven.

#### C. Create a scheduler and progress tracking application in your mobile application from part A.

Note: This can be the implementation of part A.

1. Include the following implementation requirements:
  - an arrayList
  - an intent class
  - navigation capability between multiple screens using activity
  - at least three activities
  - events (e.g., a click event)
  - the ability to work in portrait and landscape layout
  - interactive capability (e.g., the ability to accept and act upon user input)
  - a database to store and retrieve application data
  - an application title and an icon
  - the use of at least two different methods to save data (e.g., SharedPreference, SQLite)
  - notifications or alerts
  - the use of both declarative and programmatic methods to create a user interface

Note: Your application should work at least on Android 4.0 (API level 14).

2. Include at least the following interface requirements:
  - seven screens
  - the ability to scroll vertically
  - an action bar
  - a menu
  - an imageview
  - two layouts
  - input controls
  - buttons

#### D. Create a storyboard that includes each of the menus and screens from part B to demonstrate application flow.

  Storyboard is saved in the `/main/images/story_board.png`.

#### E. Provide screenshots to demonstrate that you have created a deployment package.

Note: Verify that all the required functions of your application are working by executing the apk file.

  The image saved at `/student_scheduler_mat_greten/apk_build_successful.png`

#### F. Reflect on the creation of your mobile application by doing the following:
1. **Explain mobile application development through the context of the architecture involved, including hardware and software capabilities and limitations.**
 - _Identify which version of the operating system your application was developed under and is compatible with._
   - This mobile app was written targeting API 26: Android 8.0 (O), Build Tools Version 26.0.1. Minimum Sdk Version: API 25: Android 7.1.1 (Nougat).
     - Target Sdk Version: API 26: Android 8.0 (O)
   - _During the initial development and was tested on a physical LGE Nexus 5x running API 26, Android (O), developer release 2, however due to stability issues on the device, not the Student Scheduler app, this device was rolled back to API 25, 7.1.1._
2. **Describe (suggested length of at least one paragraph) the challenges you faced during the development of the mobile application.**

  Many challenges surfaced during the development of this mobile application. While querying a SQL database now feels familiar, doing so within the context of Android using SQLite provided new challenges. Implementing cursorAdapters and custom cursorAdapters took several days of work after examining many sources. This was partially made more difficult as most resources for Android that are readily available online assume that data within the mobile app's SQLite database will not be normalized and as a result only give examples of creating, reading, updating, and deleting from a single, non-normalized table. The desire to normalize data combined with trying to combine multiple concurrent cursors to populate multiple concurrent list views within activities lead to the creation of custom cursorAdapters. These cursorAdapters were initially created as Resource Cursor Adapters with the goal of specifying which layouts would be updated within the activity, however this approach did not prove viable during the development of this project.

  The initial use of Resource Cursor Adapters rather than cursorAdapters proved troublesome throughout the remainder of the project as the overloaded Resource Cursor Adapters cased classes implementing LoaderManager.LoaderCallbacks<Cursor> to improperly implement further methods that proved confusing for learning how to use the overridden method, onCreateLoader, to refresh views when data was modified. As a result, a reliance on creating a new activity, rather than refreshing views was built into the entire application.
  The behavior that surfaced during testing was very difficult to pinpoint as each of the above issues were intertwined together at this point. When testing, selecting a term, course, or assessment would successfully load the desired item and its data, allow editing, and even saving. However when returning to the parent screen, a blank version of the item would display, rather than actually going back to the parent screen. This was due to the new activities spawned any time a view was updated. If editing an assessment note photo, after proceeding from the term, to the course, to the assessment, assessment note, and assessment photos, multiple blank items would display, making the app look like it had lost all data.

  With mounting frustration and confusion, the added bugs were recorded, but not dealt with immediately, only compounding issues. Due to the larger overall architecture problems within the application, it became easier to miss smaller best practice issues as more features were added to the app. As an example, user test input was not sanitized initially, allowing `'`s to break queries and cause the app to crash completely. These crashes confounded bug resolutions further until input was sanitized.

  The Android Studio IDE presented challenges of its own at times during development. Reliance on the GUI XML editor for activities proved frustrating for several reasons. Hooking up constraints proved unpredictable in the GUI as the render of the view would jump as constraints were changed. Often, elements would disappeared if constraints were visually reordered in the sidebar to reflect their flow of order in the activity. Element's width and height inputs often reverted back to previous values after being set.

  This project truly presented many challenges and learning opportunities.

  1. Non-sanitized input
    1. Created regex to sanitize all text user input.
    2. alert times continuing to go off seconds after setting alerts
  4. resourcecursor vs cursoradapter
  5. creating new views to display changes rather than using oncreateloader and restartLoader()
  6. fighting the XML activity render view in AS
    1. - went to using XML when the renders would crash

3. **Describe (suggested length of at least one paragraph) how you overcame each challenge discussed in part F1.**

- code path was invaluable in helping setup db provider/helper and contract for multiple tables, enabling binding views to custom cursorAdapters.
- As I could not get multiple coursors working to provide multiple list views that coudl be clicked into for Courses (course notes, mentors, assessments). I used array lists, filled by seperate queries and curors to accompoish this task
- 

4. **Describe (suggested length of at least one paragraph) what you would do differently if you did the project again.**
  1. started project with lower level API due to less resources being available for android 26.
    - Testing physical phone started out on Android 26 (O) developer preview 2 on Nexus 5x, but due to stability issues, was rolled back to Android 7.1.1.
  1. Spent multiple days trying to have multiple concurrent cursors to display different queries for list views
    1. - read more, watched more videos, performed more excercies to learn, rather than jumping right into writing code for the project to learn while tackling specific goals. straightening this out after the fact took way longer than if it had been written correctly in the first place.
  2. Knowing that Google discourages placing scrollviews within other scrollviews, I would have have laid out the flow and functionality of my application different from the very beginning.
5. **Describe how emulators are used and the pros and cons of using an emulator vs. using a development device.**
  - Pros
    - The emulator made it very easy to test using mouse and keyboard shortcuts
    - allowed for testing when I didn't have a USB-C cable to hook up to my Mac
    - allowed for easy testing of landscape orientation without accidentally un-rotating phone
  - Cons:
    - did not give a true feeling for speed, delays caused by running multiple apps for days on end without rebooting
    - emulator did not show that the database was leaking data due to not closing database properly throughout the application, but actual device did output this information
    - emulator began slowing down my machine as the app got bigger and testing sessions grew longer
      - running multiple programs with Android studio, android emulator, spotify and other utlities, crashed my delveopment machine a 2014 Macbook Pro quad-core 16 GB of memory with SSD. This machine has never crashed this hard before.

#### G. When you use sources, include all in-text citations and references in APA format.

> Note: For definitions of terms commonly used in the rubric, see the Rubric Terms web link included in the Evaluation Procedures section.

> Note: When using sources to support ideas and elements in an assessment, the submission MUST include APA formatted in-text citations with a corresponding reference list for any direct quotes or paraphrasing. It is not necessary to list sources that were consulted if they have not been quoted or paraphrased in the text of the assessment.

> Note: No more than a combined total of 30% of a submission can be directly quoted or closely paraphrased from outside sources, even if cited correctly. For tips on using APA style, please refer to the APA Handout web link included in the APA Guidelines section.

##### References List:

```
Android Camera. (2017, August 15). Retrieved September 10, 2017, from https://www.tutorialspoint.com/android/android_camera.htm
Example provided groundwork for understanding and implementing the camera. This resource was suggested by WGU class mentors with the suggestion that "You can follow the tutorial without having to make too many changes (all minor)."
```

```
Android Pickers. (2016, October 11). Retrieved September 12, 2017, from https://developer.android.com/guide/topics/ui/controls/pickers.html
Android documentation of date picker and time pickers.
```

```
Annur, B. M. (2013, October 27). How to use "Share image using" sharing Intent to share images in android? Retrieved September 10, 2017, from https://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android
Provided example of sharing multiple images at once though an intent. Adapted and added to author's code in order to share note and photos associated with note to external application of user's choice.
```

```
Creating Content ProvidersEdit PagePage History. (n.d.). Retrieved September 1, 2017, from https://guides.codepath.com/android/Creating-Content-Providers#contract-classes
Provided understanding from which the data layer of this application was built on to allow querying of database throughout the application.
```

```
Eric, A. (2012, May 24). Android-er: Cancel alarm with a matching PendingIntent . Retrieved September 12, 2017, from http://android-er.blogspot.com/2012/05/cancel-alarm-with-matching.html
Provided example of working notification cancelling after examining documentation and multiple sources did not clear this matter up.
```

```
Kothari, U. (2015, January 28). How to save bitmap from ImageView to database sqlite? Retrieved September 10, 2017, from https://stackoverflow.com/a/28186390
After attempting to convert to multiple formats, Umang's example provided working solution for translating camera input into format acceptable for storage within a SQLite BLOB type field.
```

```
Kurniawan, B. (2015). Android Application Development: A Beginners Tutorial. Retrieved September 11, 2017, from http://mmlviewer.books24x7.com/book/id_81425/viewer.asp?bookid=81425&chunkid=0224012307
Utilized throughout project, but particularly in creating, handling, and scheduling notifications.
```

```
Regular Expressions: Predefined Character Classes. (2015). Retrieved September 18, 2017, from https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html
Provided clarity on which characters would be excluded and included by performing regex matching on input strings, ultimately allowing sanitization of user input.
```

```
Sharing Content with Intents. (n.d.). Retrieved September 9, 2017, from https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
Source provided insight into using a fileprovider.xml file to allow local storage for images.
```
