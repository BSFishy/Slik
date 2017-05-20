# Slik [![Build status][2]][3] [ ![Download][4] ][5]
Slik is a Java framework for websockets, based around the Slim PHP framework. Many things were meant to be the same between the two, but there are a few key changes:

 1. The main `Slik` class is mostly static. There is no need to define an `App` variable.
 2. You can set the IP and the port for Slik to run on.
 3. You do not have to depend on external applications to run Slik. You do not have to have Apache, PHP, or whatever else you had when you were using Slim.
 4. You have more overall customisability on Slik over Slim.
 5. And the most obvious one: It's in Java!

## Using Slik as a dependency
Slik is setup with Bintray, so it is easier than ever to get official and beta releases of Slik. Use the following code in your Gradle project to use Slik:

    repositories {
        maven {
            url  "http://dl.bintray.com/lousylynx/dev" 
        }
    }
    
    dependencies {
        compile: "slik:slik:X.X.X"
    }

## Building
To build Slik, follow these simple steps:

 1. Download the Slik source code. You can do so at this [link][1].
 2. Once you are in the folder with Slik, run in the terminal: `gradlew build` for Windows, or `./gradlew build` for Linux or OSX
 3. Now Slik has been compiled into the `build/libs` folder
 
  [1]: https://github.com/LousyLynx/Slik/archive/master.zip
  [2]: https://travis-ci.org/LousyLynx/Slik.svg
  [3]: https://travis-ci.org/LousyLynx/Slik
  [4]: https://api.bintray.com/packages/lousylynx/dev/Slik/images/download.svg
  [5]: https://bintray.com/lousylynx/dev/Slik/_latestVersion