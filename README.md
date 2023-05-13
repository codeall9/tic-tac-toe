# Tic Tac Toe
A simple Tic Tac Toe app build with jetpack compose

<div style="display:flex;">
  <img src="./screenshots/demo01.png" width="30%" hspace="8">
  <img src="./screenshots/demo02.png" width="30%" hspace="8">
  <img src="./screenshots/demo03.png" width="30%" hspace="8">
</div>

# Architecture
The architecture is follows the [Onion architecture](https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/) approach for several reasons:
- **Clear Separation of Concerns**: The architecture helps in separating the core business logic of an application from the peripheral concerns, like the UI, database, or external services. This makes the application easier to understand, maintain, and modify.
- **Independent of Frameworks**: The architecture does not depend on the existence of some library of feature laden software. This allows us keep evolving the project and experience new tech ex: Jetpack compose, Kotlin Multiplatform.
- **Testability**: The architecture promotes testability by making it easier to write unit tests for different parts of the application.

<div style="display:flex;">
  <img src="./screenshots/architecture.png" width="50%">
</div>

# Modularization
This project contains the following types of modules:
- The app module (User interface): Android application which contains all the UI code for now. The app module will depends on infra modules or core modules.
- core modules (Application Core): contains business logics and common functions to be shared between other modules. These modules should not depend on any framework or 3rd services, ex: Android SDK
- infra modules (Infrastructure): connects our application core to tools like a database, 3rd party APIs or framework APIs.
- others: custom plugins and buildSrc to share gradle build logics and configuration

# Testing
Most of the unit test generate random input and repeat multiple times to cover the extreme cases, ensure the functionality is correct.

# Contributing
Pull requests are welcome. This project is for practicing and learning new things, you can also open an issue for feedback or discussion.
