# BetonQuest Editor

**This will most likely not get updates anymore!!**

This is the official **legacy** editor for BetonQuest plugin. It was created to speed up the process of writing quests, which can be tedious and error-prone work when editing files directly in a text editor.


![Editor](http://betonquest.betoncraft.pl/images/editor/conversations.png)

## Features

* Editing all features of a quest package
* Easy conversation editing
* Autocomplete suggestions for pointers, events, conditions etc.
* Cross-package references support
* Highlighting incorrect objects
* Saving/loading to/from _.zip_ files and uncompressed directories
* Package exporting with custom shell script (not included of course)
* Quick translating of a package to another language(s)

## Future updates

You can check out the list of planned features [here](https://github.com/Co0sh/BetonQuest-Editor/milestone/2). If you have any new ideas feel free to open new issue and we'll discuss it together.

## Where to buy?

[Store page on Sellfy](https://sellfy.com/p/nE5Y/)

## Why isn't it free?

It is - you can compile it for yourself (details below). But I encourage you to buy it anyway. That way you don't have to get in this whole compiling mess and can download a nice executable file. Also all future updates will come to you via an e-mail, no need to compile anything here either. And by the way, you're supporting me and giving a little something for all the time spent coding BetonQuest plugin and editor.

## Installation

You don't have to install this program. The only thing you need is Java 8 (or newer) installed on your system. If you don't have it (the program doesn't start), you can download it from [here](https://java.com/download). To start the editor simply double-click on its icon. This program works on Windows, Mac and Linux, as long as Java 8 is installed.

## Usage

A detailed manual can be foun on the project [wiki](https://github.com/Co0sh/BetonQuest-Editor/wiki). To successfully create quests with this editor you need to know basics of BetonQuest. I highly recommend reading its [documentation](http://betonquest.betoncraft.pl/BetonQuestDocumentation.pdf) before, even though it describes editing the files directly. In the future I will probably add built-in tutorials and/or video guides for the editor.

Check out [BetonQuestUploader](https://github.com/Co0sh/BetonQuestUploader), designed as a bridge between the editor and the plugin. It can allow you to let many users design quests without access to FTP.

## How to compile?

All you need to compile BetonQuest Editor on any platform is copy of the [source code](https://github.com/Co0sh/BetonQuest-Editor/archive/master.zip), installed [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [Maven](https://maven.apache.org/download.cgi) (binary [added to path](https://google.com/search?q=add+file+to+path)). Open system console, navigate to source code folder and issue `mvn package` command. The compiled _.jar_ file will appear in _target/_ directory after a few seconds.

**You are allowed to compile this program even if you didn't buy it!**

## License

BetonQuest Editor is an open source project licensed under [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl.txt). This makes it free as in "free speech". You can buy it to support me or compile it for yourself if you can't spend the money. You are also most welcome to contribute anything - translations, code, CSS styling - as long as you don't mind me making a little money on it.

## Bugs

This project is fairly new and not everything is guaranteed to work correctly. If you ever find a bug or an error shows up, please post it on the [Issues](https://github.com/Co0sh/BetonQuest-Editor/issues) or send it via e-mail. It will really help me with fixing bugs.
