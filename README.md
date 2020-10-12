# MarioSimulatorInteractiveReviver
A project to make SMG4 Mario Simulator Interactive into a playable android app using YouTube API.
To learn more about YouTube API see this: https://developers.google.com/youtube/android/player

# Why I made this project?
I've been watching SMG4 videos since 2015. Back then I was really disappointed that I can't play the
Mario Simulator Interactive as intended on my smartphone. This is because annotations didn’t
work on mobile. I had a dream that maybe one day I can do something about it.

In 2019 I took a course the teach how to make android app using android studio. During the course
the idea of getting the simulator to work on a mobile came back to my head. It has become much more
relevant after that youtube stopped showing annotations and the simulator become not playable as
 intended. (https://mashable.com/article/the-death-of-youtube-annotations/)

After I finally had enough time, I started working on this project - The Mario Simulator Interactive
Reviver. The main goal of this project is to make the app similar to the original simulator as much
as possible.

Initially, I wanted to put the buttons on the video screen, but the YouTube API don't allow to put
another view on the youTubePlayerView.

The core work in this project was the database. It took several days to document all the relevant
information of each video of the 56 videos in one huge json file (simulator_navigation.json).
Luckily SMG4 put almost all the simulator videos in a playlist (https://www.youtube.com/playlist?list=PLqtXS2zBPMiTCrDc-LSWr03fJSx916koi)
and I had @PKMaster41 video to aid me (https://www.youtube.com/watch?v=vknAtaZJB2M).

# How to make it work?
You need to get your own Android API key and put it on Constants.java. Then you can run the app on
the android studio.

# How to continue from here?
