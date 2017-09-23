# MOBILE APPLICATIONS DEVELOPMENT

## Introduction to Mat Greten's Student Scheduler
Written portions for this assignment are included in this readme underneath section **F**.

This assignment included a checklist and assignment requirements. Examples of how and where these requirements were met are provided below under section **C**, where applicable.

File Locations:
- Unsigned APK file is located at `/Student_scheduler_mat_greten/app-debug.apk`
- Signed APK file is located at `/Student_scheduler_mat_greten/app-release.apk`
- Mobile App Storyboard is located at `/main/images/story_board.png`.

Compatibility:
- This app was written targeting API 26: Android 8.0 (O), Build Tools Version 26.0.1.
  - Minimum Sdk Version: API 25: Android 7.1.1 (Nougat)
  - Target Sdk Version: API 26: Android 8.0 (O)

_During the initial development and testing, the mobile app was tested on a physical LGE Nexus 5x running API 26, Android (O), developer release 2, however due to stability issues on the device, not the Student Scheduler app, this device was rolled back to API 25, 7.1.1. After this, testing was performed on a virtual Nexus 5x and physical LGE Nexus 5x, both running API 25 Android 7.1.1._


## Mobile Application Development

### Requirements:

_Note: Submit your performance assessment by including all Android project files, APK, and signed apk files in one zipped.zip file._

- All project files generated while completing this project can be found within `/student_scheduler_mat_greten/Student_scheduler_mat_greten`

- Unsigned APK file is located at `/Student_scheduler_mat_greten/app-debug.apk`
- Signed APK file is located at `/Student_scheduler_mat_greten/app-release.apk`

**Note**: For your convenience an optional checklist is attached to help guide you through this performance assessment.

### Checklist

#### C. Create a scheduler and progress tracking application in your mobile application from part A.

1. Include the following implementation requirements:
  - an arrayList
    - An arrayList is used within the `CourseNoteEditorActivity.java` class to share notes to external applications.
    - Another example of an arrayList in this mobile application can be found within the `getTermTitles()` method on lines 323 - 335 of the class `CourseEditorActivity.java`.
  - an intent class
    - Intents are used throughout the application to move between activities, while sharing information between activities.
      For example: the class `AssessmentEditorActivity.java` implements the following method in lines 439 - 455 to allow both the Assessment ID and Title to flow through to the AssessmentAlertActivity:
      ```
      public void openAssessmentAlertsList(View view) {
        switch (action) {
            case Intent.ACTION_INSERT:
                Toast.makeText(this, R.string.save_assessment_first, Toast.LENGTH_LONG).show();
                break;
            case Intent.ACTION_EDIT:
                alertIntent = new Intent(AssessmentEditorActivity.this, AssessmentAlertActivity.class);
                Uri uri = Uri.parse(AssessmentAlertEntry.CONTENT_URI + "/" + id);
                alertIntent.putExtra(AssessmentEntry.CONTENT_ITEM_TYPE, uri);
                alertIntent.putExtra("assessmentTitle", oldTitle);
                alertIntent.putExtra("assessmentFKID", assessmentID);
                startActivityForResult(alertIntent, EDITOR_REQUEST_CODE);
                System.out.println(uri);
                System.out.println("ASSESSMENT ID IS: " + assessmentID);
                break;
        }
    }
      ```
    - The class `AlertHandler.java` was implemented to specifically allow scheduled notifications to trigger an intent to surface these notifications at a later time.
  - navigation capability between multiple screens using activity
  - at least three activities
  - events (e.g., a click event)
  - the ability to work in portrait and landscape layout
    - Several landscape layout files are included within `app/res/layouts` to accommodate special landscape only versions of layouts to better use the wider screen.
    - Landscape versions of layout files are labeled like this example: `activity_mentor_editor.xml (land)`.
  - interactive capability (e.g., the ability to accept and act upon user input)
  - a database to store and retrieve application data
    - There are three classes used in this project to allow interaction with the application database:
      1. ScheduleContract.java
      2. ScheduleDBHelper.java
      3. ScheduleProvider.java
  - an application title and an icon
    - `AndroidManifest.xml` lines, 12, 13, and 14 specify the applications title and icons.
  - the use of at least two different methods to save data (e.g., SharedPreference, SQLite)
    - This application provides two ways of saving data:
      1. The SQLite database named `schedule.db`.
      2. AssessmentNotes and CourseNotes both provide the ability to export either the text associated with the notes or both the text and the photos associated with these notes.
  - notifications or alerts
    - AlertManager is utilized both in the `CoursesEditorActivity.java` class and the `AssessmentAlertEditorActivity.java`class to schedule notifications for later. The class `AlertHandler.java` acts as the receiver to handle these managed alerts.
  - the use of both declarative and programmatic methods to create a user interface
    - Numerous interface controls are implemented through the various XML layout files in a declarative fashion. - Examples of programmatic user interface creation:
      - Menus within editor activities are chosen programmatically depending on whether the item being edited is a new item or an item that already exists. programmatically depending on whether the item being edited is a new item or an item that already exists.
    - Within each editor activity, if the item is not a new item, the values of this item are retrieved from the database cursor and text views and text input fields are populated from this information.
    - When course are loaded for editing, their term is loaded in a spinner at load time.
    - When assessments are edited, they display both the course in a spinner and the course due date in a textview. When a different spinner option is chosen, the course date programmatically updates to match the selected course.

2. Include at least the following interface requirements:
  - seven screens
  - the ability to scroll vertically
    - The ability to scroll vertically is included in several places where appropriate within this application:
      - The `CoursesEditorActivity.java` class provides this ability as it contains information for the course, course notes, course mentors, and course assessments.
      - This ability also is implemented over photos in both the `CourseNoteEditorActivity.java` class and `AssessmentNoteEditor.java` class.
      - The main selection screens for Terms, Courses, Assessments, Course Notes, Cours Mentors, and Assessment Alerts contain this ability as well.
  - an action bar
    - Throughout the application the action bar is used to host the save and cancel buttons from the menu present in all editor screens.
    - The titles in most activity screens is also set by interacting with the action bar.
      - Example: `AssessmentAlertActivity.java` line 66 `getSupportActionBar().setTitle("AssessmentAlerts");`.
  - a menu
    - Three menus are implemented throughout the application's activity screens and are surfaced depending on the context.
      - `menu_editor.xml` is implemented in all Editor Activities.
      - `menu_insert.xml` is utilized within all Editor screens and is applied when the Editor is creating a new object (Term, Course, Assessment).
      - Examples of both menu_editor and menu_insert being implemented can be found in `CoursesEditorActivity.java` Lines 558 - 566:
      ```
      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)) {
            getMenuInflater().inflate(R.menu.menu_insert, menu);
        }
        return true;
    }
      ```
      - `menu_list.xml` is inflated within the `TermsActivity.java` class and provides the delete option that exposed in the action bar.
  - an imageview
    - Image views are used to surface the photos taken in both AssessmentNotes and CourseNotes.
    - `item_course_photo.xml` and `item_assessment_photo.xml` each provide a default image icon to indicate a photo has not been taken yet, once a photo is taken, this imageview is used to surface the newly taken photo.
  - two layouts
    - Many unique layouts are implemented in this project. These files can be located in the `/app/res/layout` folder. Landscape layouts are also included at this location as well.
  - input controls
  - buttons

#### D. Create a storyboard that includes each of the menus and screens from part B to demonstrate application flow.

  Storyboard is saved in the `/main/images/story_board.png`.

#### E. Provide screenshots to demonstrate that you have created a deployment package.

  The image saved at `/student_scheduler_mat_greten/apk_build_successful.png` shows the Android Studio output indicating that a Signed APK was successfully generated. Screenshots used within the storyboard were captured from the APK running on a physical Nexus 5x.

#### F. Reflect on the creation of your mobile application by doing the following:

##### 1. Explain mobile application development through the context of the architecture involved, including hardware and software capabilities and limitations.

  Mobile application development provides many opportunities for developers that are not readily present when developing applications for desktop or web environments. Now that smartphones can be found within nearly everyone's pockets; cameras, motion-sensors, light-sensors, audio-recoding devices are already present as well. These new sensors and input methods provide opportunity for developers as these readily
  available resources can be integrated into applications to make them more useful and robust. While easily available, these devices do not all run the same software. With this in mind Android applications must be developed with compatibility in mind for several reasons. While most phones now have cameras, not every phone will have one while others will have several that must be controlled appropriately. The Android environment has sought to provide better user experiences in later versions through preventing permissions over-reach. This requires developers to ask users for permissions from users when doing common tasks like taking pictures or writing to the file-system. While Java can be a very powerful framework as it can run in many environments it can also be very easy to leave connection open or not properly close down resources, which becomes crucial within in the context of mobile computing. Today's Android devices are powerful and energy-efficient, however inefficient applications that reach for to many permissions or are not crafted to efficiently handle application logic can lead to very frustrating user experiences on mobile devices. Meeting the multiple challenges presented by mobile development provides the rewarding the pleasure of physically holding an application within one's hand.

  **Identify which version of the operating system your application was developed under and is compatible with.**
   - This mobile app was written targeting API 26: Android 8.0 (O)
   - Build Tools Version 26.0.1.
   - Minimum Sdk Version: API 25: Android 7.1.1 (Nougat).
   - Target Sdk Version: API 26: Android 8.0 (O)


   _During the initial development and testing a physical LGE Nexus 5x running API 26, Android (O), developer release 2 was used, however due to stability issues on the device, not the Student Scheduler app, this device was rolled back to API 25, 7.1.1._


##### 2. Describe (suggested length of at least one paragraph) the challenges you faced during the development of the mobile application.

  Many challenges surfaced during the development of this mobile application. While querying a SQL database now feels familiar, doing so within the context of Android using SQLite provided new challenges. Implementing cursorAdapters and custom cursorAdapters took several days of work after examining many sources. This was partially made more difficult as most resources for Android that are readily available online assume that data within the mobile app's SQLite database will not be normalized and as a result only give examples of creating, reading, updating, and deleting from a single, non-normalized table. The desire to normalize data combined with trying to combine multiple concurrent cursors to populate multiple concurrent list views within activities lead to the creation of custom cursorAdapters. These cursorAdapters were initially created as Resource Cursor Adapters with the goal of specifying which layouts would be updated within the activity, however this approach did not prove viable during the development of this project.

  The initial use of Resource Cursor Adapters rather than cursorAdapters proved troublesome throughout the remainder of the project as the overloaded Resource Cursor Adapters caused classes implementing `LoaderManager.LoaderCallbacks<Cursor>` to improperly implement further methods that proved confusing when learning how to use the overridden method, onCreateLoader, to refresh views when data was modified. As a result, a reliance on creating a new activity, rather than refreshing views was built into the entire application.

  The subsequent behavior that surfaced during testing was very difficult to pinpoint as each of the above issues were intertwined together at this point. When testing, selecting a term, course, or assessment would successfully load the desired item and its data, allow editing, and even saving. However when returning to the parent screen, a blank version of the item would display, rather than actually going back to the parent screen. This was due to the new activities spawned any time a view was updated. If editing an assessment note photo, after proceeding from the term, to the course, to the assessment, assessment note, and assessment photos, multiple blank items would display, making the app look like it had lost all data.

  With mounting frustration and confusion, the added bugs were recorded, but not dealt with immediately. This only compounded the issues. Due to the larger overall architecture problems within the application, it became easier to miss smaller best practice issues as more features were added to the app. As an example, user text input was not sanitized initially, allowing `'`s to break queries and cause the app to crash completely. These crashes confounded bug resolutions further until input was sanitized.

  The Android Studio IDE presented challenges of its own at times during development. Reliance on the GUI XML editor for activities proved frustrating for several reasons. Hooking up constraints proved unpredictable in the GUI as the render of the view would jump as constraints were changed. Often, elements would disappear if constraints were visually reordered in the sidebar to reflect their flow of order in the activity. Element's width and height inputs often reverted back to previous values after being set.

  This project truly presented many challenges and learning opportunities.

##### 3. Describe (suggested length of at least one paragraph) how you overcame each challenge discussed in part F1.

  Overcoming the challenges presented throughout this project required extensive trial and error as well as several hours of researching the Android Developer documentation manuals regarding best practices regarding limitations and specifications when developing in Java for Android. After many failed implementations, the Codepath (2015) content provider tutorial provided the foundation for building a custom contract class utilizing two tables. This tutorial provided clarity with the issues that were preventing bound cursorAdapters from querying individual query results and also assisted in creating a naming scheme throughout the application, allowing for consistent, repeatable access to desired columns in cursor query results.

  For a large portion of this projected, a particularly nasty bug was not diagnosed and dealt with. Due to difficulties implementing cursorAdapters and updating list views when modifying the underlying database, new activities were started as a temporary workaround rather than truly implementing working cursors that dynamically updated list views. Allowing this bug to persist was a mistake that only continued to grow with each additional activity that was added to the project. Waiting to resolve this bug till later rather than sooner meant that this bug was intertwined within most project class files and made diagnose only more difficult. When this problem was finally approached again, the problem seemed insurmountable. However stepping through methods, printing output to the console, and several hours worth of retracing footsteps through the instantiation of the simplest classes ultimately revealed several problems that were creating so much confusion.

  The onCreateLoader methods in each class extending LoaderManager were returning exactly what was being asked for when modifications were made to database entries; search terms that returned null results. Further, workarounds implemented early on with the goal of having multiple dynamically updated list views in the same activity utilized the resourceCursorAdapater class, rather than the more simple cursorAdapter class. As a result, views were being written over by the explicit passing of view ids when instantiating cursorAdapters. Implementing CursorAdapater allowed reliance on getLoaderManager and restartLoader to finally fully work, allowing dynamically updated results in activities.

  After facing several days of difficulties when trying to embed multiple dynamic cursors, list views, and scrollviews on the same difficulty, a different approach was taken to surface content both in the CoursesEditor activity and AssessmentsEditor Activity classes. Within the Course Editor class, course mentors and course notes were implemented as arrayLists that populated list views, while course assessments were loaded through a dynamic cursor to populate a list view. Within the assessments class the same approach was used to populate assessment alerts as an arraylist whereas assessment notes were implemented using a dynamically updated list view. This method allowed for necessary data to be displayed on the screen, with the most crucial information truly, being dynamic, whereas less pertinent information could be surfaced by an arraylist of strings representing the titles of objects in the list, allowing users to click into the list for more details. The version control software Git proved invaluable during the process of tackling the large structural issues in this mobile application.

  One very useful tool that assisted in overcoming the challenges above was Git. Git allowed the use of diffs to discover when bugs were introduced or when a variable was mistakenly rename throughout the project. The ability to step back through previous changes ultimately enabled the untangling of the several miss-applied workarounds.

  Due to the large structural issues that were long lasting during development, input sanitization was not accounted for until late in the process. Single quote's included in the application's text input fields were successfully saved and retrieved from the database, however subsequent queries and joins using fields with these characters would result in full crashes of the mobile application. To overcome this issue a regex matching class was introduced to the application to sanitize user input, `InputValidation.java`. The regex matching methods allow uses to enter any characters, however only approved character will remain upon submission of data through he methods, which are applied to each user text input field.

  Just like any IDE, Android Studio brought with it its own flavor of difficulties to the project. The inconsistent experience with the XML rendering for activity layouts was remedied by mainly relying on editing the raw XML rather than rendering these files. At times, Android Studio would be hampered by incorrectly typed variable or mismatched constraint, bringing the entire IDE down. When this occurred, examining and editing the suspect XML file within Atom Editor provided a way through to fix the files and allow Android Studio to launch again.

  The process of developing this mobile application provided many memorable lessons that will be utilized in software projects for many years to come.

##### 4. Describe (suggested length of at least one paragraph) what you would do differently if you did the project again.

  As this project presented many learning opportunities, approaching this project again would look quite different form the initial approaches reflected upon above.

  One large difference in approach would be starting out targeting a lower level API as it is now evident that there are currently less resources available for still new API 26. Testing  began on this approach with a physical phone Android 26 (O) developer preview 2 on Nexus 5x, but due to stability issues, this phone was rolled back to Android 7.1.1 as this phone is also used for work and personal daily use. Had this issue been foreseen, some time would have been saved in initially targeting devices with older, robustly documented, and more easily available versions of Android.

  During the beginning development of this project, several days were lost to misunderstanding limitation of the Android system regarding list views, cursors, and scrollviews.If starting anew, lest time would be spent aiming for multiple concurrent cursors to display different queries for list views. While that sounds naive, this would be preventable had more initial research, reading, and testing been performed before project code was written. Dutifully performing more exercises to solidify core Android concepts, rather than jumping right into writing code for the project would have sped development up and minimized the introduction of time-consuming bugs into the codebase. Attempting to learn the complexities of list views and cursorAdapters while having already built up a custom context and environment only made tackling specific goals at the same time more difficult.

  While it was helpful from a contextual and conceptual stance to build out all screens before having full functionality established this is not how this project would be approached a second time. Rather, focusing on building out a term or course screen and editor's methods fully, while stubbing out the dependancies on each of the notes, assessments, and other data points, would have ensured that the most basic and fundamental operations of the application were fully working before spreading faulty code throughout. Having a solid understanding of how Android implements graphic representations of database entries, this application would have had solid, repeatable methods for displaying this from the start, rather than requiring haphazard refactoring of every class to get basic information to show up correctly on screen.

##### 5. Describe how emulators are used and the pros and cons of using an emulator vs. using a development device.

  Emulators provide great flexibility for developers as applications can be tested quickly in multiple software environments. This is extremely helpful in light of Android ecosystem's largely fragmented segments of devices running older operating systems. Emulators help bridge these gaps without having the same overhead in cost. Emulators in general allow developers to program in nearly any operating system, while opening up the possibility of targeting for other operating systems.

  This project highlighted several helpful aspects of emulators. One positive ability provided by running Android in an emulator on a development computer is that the emulator made it very easy to test using mouse and keyboard shortcuts. This particularly allowed for quick entry of user input into multiple screens in a manner that is not possible when using a physical device alone for testing. Additionally, testing within an emulator allowed for testing when a USB-C cable was not available in various environments to hook up the Nexus 5x to a pre-USB-C Macbook Pro. Testing landscape orientation became much easier as the emulator could not be tipped, causing accidental orientation changes that often happen when using a physical phone for testing.

  While using an emulator does provide many benefits, emulators do have downsides as well. One such downside is that an emulator cannot easily give a true feeling for speed issues and delays caused by running multiple apps for days on end without rebooting. Real world, physical devices give more realistic view of application performance as unlike an emulator, they do not exist within a vacuum. This project also revealed that Android emulator did not always show the same console output that a real world device would. The SQLite database underneath this application was leaking data due to not being closed properly throughout the application, however the emulator did not pick up on this, whereas the real Nexus 5x made this very clear.

  Using an emulator is not truly free. While much more cost-effective monetarily, an emulator takes an extra toll on the development system as it siphons memory and CPU cycles to not only run the application being test, but also the entire mobile device's operating system. This became apparent when the emulator began slowing down the development machine for this project as the app got bigger and testing sessions grew longer. Running multiple programs with Android studio, the Android emulator, Spotify and other utilities eventually crashed the development machine, a 2014 Macbook Pro quad-core 16 GB of memory with SSD and no previous history of full system crashes. Testing on the physical Nexus 5x became necessary towards the end of the project as the device handled all application logic, operating system logic, and console output back to Android Studio.

  Ultimately, emulators provide flexibility and great opportunities, but also introduce new overhead and costs that must be considered during the development process.

#### G. When you use sources, include all in-text citations and references in APA format.

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
(2015). Creating Content ProvidersEdit PagePage History. (n.d.). Retrieved September 1, 2017, from https://guides.codepath.com/android/Creating-Content-Providers#contract-classes
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
