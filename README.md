Nukkit
===================
![nukkit](https://github.com/Nukkit/Nukkit/blob/master/images/banner.png)

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


__Nukkit is a Server Software For Minecraft: Pocket Edition__

[![PayPayl donate button](https://img.shields.io/badge/paypal-donate-yellow.svg)](https://www.paypal.me/MagicDroidX)
[![Gitter](https://img.shields.io/gitter/room/Nukkit/Nukkit.js.svg?style=flat)](https://gitter.im/Nukkit/Nukkit)
[![Travis](https://img.shields.io/travis/Nukkit/Nukkit.svg?style=flat)](https://travis-ci.org/Nukkit/Nukkit)
[![Jenkins](https://img.shields.io/jenkins/s/https/ci.itxtech.org/Nukkit.svg)](https://ci.itxtech.org/job/Nukkit/lastSuccessfulBuild/)

-------------

Download Nukkit:
-------------
* __[Official Site](https://nukkit.io)__
* __[Circle CI](https://circleci.com/gh/Nukkit/Nukkit/tree/master/)__ (**login required**)
* __[Jenkins](https://ci.itxtech.org/job/Nukkit/lastSuccessfulBuild/)__

Introduction:
-------------

Nukkit is software for Minecraft: Pocket Edition.
It has a few key advantages over PocketMine-MP:

* Nukkit is written in Java, which is faster than PhP.
* Rewriting PocketMine plugins and Bukkit plugins are super easy because of Nukkit's similar API.

Nukkit is currently stable, we welcome contributions however.

Compile Nukkit:
-------------
You can compile Nukkit with maven.
First select a dictionary.
Clone Nukkit in the dictionary, then execute these commands:
- `git submodule update --init`
- `mvn clean`
- `mvn package`

How to run:
-------------
Execute `java -jar Nukkit.jar`
Alternativly, you can create a start.sh or a start.cmd file.

Plugin API
-------------
####**Example Plugin**
Here is a Example Plugin to demonstrate Nukkits's API:

* __[Example Plugin](http://github.com/Nukkit/ExamplePlugin)__

Devtools
-------------
Here are some useful developer tools for Nukkit:

* __[FDevTools](https://github.com/fengberd/FDevTools)__ (**Load source and pack them easily**)
* __[PocketServer](https://github.com/fengberd/MinecraftPEServer)__ (**Run Nukkit on android devices**)

Discussion & Fourms
-------------
* __[Forums](https://forums.nukkit.io)__
* __[百度 Nukkit 吧](http://tieba.baidu.com/f?kw=nukkit)__
