# MarioSimulatorInteractiveReviver
A project to make [SMG4](https://www.youtube.com/user/supermarioglitchy4) [Mario Simulator Interactive](https://youtu.be/EFeeKPXC-HA) into a playable android app using YouTube API. To learn more about YouTube API see [this](https://developers.google.com/youtube/android/player).

# Why I made this project?
I've been watching SMG4 videos since 2015. Back then I was really disappointed that I can't play the Mario Simulator Interactive as intended on my smartphone. This is because annotations didn’t work on mobile. I had a dream that maybe one day I can do something about it.

In 2019 I took a course the teach how to make android app using android studio. During the course the idea of getting the simulator to work on a mobile came back to my head. It has become much more relevant after that youtube [stopped showing annotations](https://mashable.com/article/the-death-of-youtube-annotations/) and the simulator become not playable as intended.

After I finally had enough time, I started working on this project - The Mario Simulator Interactive Reviver. The main goal of this project is to make the app play similar to the original simulator as much as possible.

Initially, I wanted to put the buttons on the video screen, but the YouTube API don't allow to put another view on the youTubePlayerView.

The core work in this project was the database. It took several days to document all the relevant information of each video of the 56 videos in one huge json file (simulator_navigation.json).
Luckily SMG4 put almost all the simulator videos in a [playlist](https://www.youtube.com/playlist?list=PLqtXS2zBPMiTCrDc-LSWr03fJSx916koi) and I had [@PKMaster41](https://twitter.com/PKMaster41) [video](https://www.youtube.com/watch?v=vknAtaZJB2M) to assist me.

# How to make it work?
You need to get your own Android API key and put it on Constants.java. After that android studio will be able to run the app.

# How to continue from here?
I wish I had more time to work on this project, but I can't. But maybe you can.
There a lot of things to be work on:
* Change option buttons to be more like controller buttons.
* Change the design to be more appealing.
* Make more accurate time stamps on database.
* Count the destroyed toads and show it in the end of the game.
* Make a game over activity
and much more.
If you have the knowledge and the motivation feel free to fork this project and continue my work. Feel free to contact me on twitter [@aviadud](https://twitter.com/aviadud).

# SMG4 if you reading this:
Big fan of your old work. And thank you for inspiring new generation of machinimists. 

