Nukkit
===================
![nukkit](https://github.com/MagicDroidX/Nukkit/raw/master/images/banner.png)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.


__A Nuclear-Powered Server Software For Minecraft: Pocket Edition__

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Nukkit/Nukkit)
[![Build Status](https://travis-ci.org/Nukkit/Nukkit.svg)](https://travis-ci.org/Nukkit/Nukkit)

-------------

Get Nukkit
-------------
* __[Jenkins](http://ci.mengcraft.com:8080/job/nukkit/)__ powered by **MengCraft**


Introduction
-------------
Nukkit is nuclear-powered (java-based) server software for Minecraft: Pocket Edtion.
It has a few key advantages over PocketMine-MP:

* Nukkit is written in java, this makes it more fast and stable.
* Nukkit has a similar structure to PocketMine-MP, because of this it's easy to contribute to Nukkit's development.
* Nukkit has a similar API that PocketMine has. This makes porting plugins very easy.
* Nukkit has a different networking system, allowing faster Networking.

However, Nukkit is **not finished** yet, and we are currently testing it.
Please report any issues or create a pull request to help contribute.

Build JAR file
-------------
You need to compile Nukkit into a jar file. An IDE can do this.
- Init modules by `git submodule update --init`
- Compile by `mvn clean package`

Running
-------------
Running Nukkit is very simple.
First, drop the Nukkit.jar phar in a folder.
Use terminal and locate that folder using `cd dictionary/dictionary`.
Simply run `start.sh` or `start.cmd`. You can also excecute `java -jar Nukkit.jar`.
After you run it for the first time, select a language and then Nukkit will start up!

Plugin API
-------------
####**Example Plugin**
Example Plugin that shows you Nukkit's API:

* __[Example Plugin](http://github.com/Nukkit/ExamplePlugin)__

一起来讨论Nukkit！
-------------
* __欢迎加入[百度 Nukkit 吧](http://tieba.baidu.com/f?kw=nukkit)__
