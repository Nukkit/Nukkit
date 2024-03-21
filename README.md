![nukkit](https://github.com/Nukkit/Nukkit/blob/master/images/banner.png)

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)
[![Discord](https://img.shields.io/discord/393465748535640064.svg)](https://discord.gg/5PzMkyK)

# ATTENTION
-------------
This repository is no longer actively maintained. For our latest project, please check out **[🌟Project Endstone](https://github.com/EndstoneMC/endstone)**, which allows you to write plugins for Bedrock Dedicated Server using C++ and Python 🐍. It comes with cross-platform capability and supports all the latest Minecraft features.

# Introduction
-------------
Nukkit is nuclear-powered server software for Minecraft Bedrock Edition with several advantages over other server software. It's written in Java, which makes Nukkit faster and more stable. Moreover, its friendly structure makes it easy to contribute to Nukkit's development and rewrite plugins from other platforms into Nukkit plugins. Active maintenance of Nukkit takes place at [CloudburstMC Nukkit](https://github.com/CloudburstMC/Nukkit).

# Build JAR file
-------------
To build a JAR file, follow the steps below. Please ensure you have Java Development Kit (JDK) and Maven installed in your system before executing these steps:
- `git submodule update --init`
- `mvn clean package`

# Running
-------------
To run the software, simply execute either `start.sh` or `start.cmd`, or run the command `java -jar Nukkit.jar`.

# Plugin API
-------------
Below is an Example Plugin showing the API of Nukkit.
- [Example Plugin](http://github.com/Nukkit/ExamplePlugin)


# Contributing
------------
If you wish to contribute to our project, please first read our [CONTRIBUTING](.github/CONTRIBUTING.md) guide. Issues that come with insufficient information or not in the given format will be closed and will not be reviewed.
