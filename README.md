# CS310Project
Our emulator was AVD name: pixel 2 API 24
it used Nougat Android 7.0 Arm64-v8a
VM heap: 500 MB

In our AndroidManifest.xml it is essential to add these lines in <application>:
  android:hardwareAccelerated="false"
  android:largeHeap="true"
  
firebase & emulator issues:
sometimes emulator stops working because it has trouble communicating with Firebase. Please stop and run again
app functionality might take longer to load sometimes. 
specifically in the classes tab, if more information is not displayed by clicking on the toggle button, might have to click a couple more times for it to show up

our registered users:
1. email: shahra@usc.edu    password: gaurishahra
2. email: pingle@usc.edu    password: bubbles
3. email: mehtarhe@usc.edu  password: rheamehta
4. email: banka1@usc.edu    password: banka1

chat tab:
shows the list of students you have ongoing chats with and clicking on the name redirects to the ongoing chat
each student also has a view profile (which redirects to a friend's profile info) and a block user button 

profile tab:
can update all information. can also log out 

classes tab:
shows a list of departments and clicking on the "+" button redirects to all the classes. clicking on the "+" button for each course will display more information. 
"see reviews" will redirect to a page with all reviews and give the option to add your own review. 
The "enroll" button adds a student to the roster and allows u to "see the roster"
"see roster" will redirect to a page with a roster of enrolled users. u can start chatting with users on this page
