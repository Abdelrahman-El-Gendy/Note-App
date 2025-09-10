# 🗒️ Note-App

A modern Android note-taking application built with **Kotlin** and **Jetpack Compose**, following Clean Architecture principles. Note-App helps users quickly jot down, edit, delete, and organize notes with a beautiful and responsive Material 3 design.

---

## 🚀 Features

- ✅ Create, update, and delete notes
- 🎨 Organize notes by color
- 🔍 Search and sort notes by date or title
- 🧹 Swipe to delete with Undo via Snackbar
- 💾 Offline data persistence using Room
- 🎨 Clean Material 3 UI with animations
- 🔄 MVVM + Clean Architecture with DI and Coroutines

---

## 📸 ScreenRecord

### This is not the final verison, you can download it from release to test the final version

>[ *Add screenshots or screen recordings here to showcase your app's interface and features.*](https://github.com/user-attachments/assets/dc914bea-1f3e-402c-ab31-ef533f08a80d)

---

## 🛠️ Technologies Used

- **Kotlin** – Main programming language
- **Jetpack Compose** – Declarative UI toolkit
- **Room** – Local database storage
- **Hilt** – Dependency injection
- **Navigation Compose** – Navigation between screens
- **Coroutines & Flow** – Asynchronous and reactive programming
- **Material 3** – Modern UI components
- **Android Studio** – Development environment

---

## 🏗️ Architecture

The app is structured using **MVVM** and **Clean Architecture**, with three clearly separated layers:

- **Presentation Layer**: UI logic with Composables and ViewModel
- **Domain Layer**: Use cases and business rules
- **Data Layer**: Repositories, Room DB, and data models

This separation ensures maintainability, testability, and scalability.

---

## 🧰 Getting Started

### Prerequisites

- Android Studio Giraffe or newer
- Android SDK 33+
- Kotlin 1.9+
- An Android device or emulator

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Abdelrahman-El-Gendy/note-app.git

2. Open the project in Android Studio

3. Let Gradle sync and install dependencies

4. Run the app on an emulator or physical device

## 📝 Usage

* Tap the ➕ Floating Action Button to add a new note
* Tap a note to edit its content
* Use the menu to toggle sorting and order settings
* Swipe a note to delete it and undo if needed

## 🤝 Contributing
Contributions are welcome!

If you'd like to contribute:

# Fork this repo

* Create a feature branch (git checkout -b feature/YourFeature)
* Commit your changes (git commit -m 'Add YourFeature')
* push to the branch (git push origin feature/YourFeature)
* Open a Pull Request

- Please follow Kotlin best practices and keep code clean and modular.

## 📄 License
- This project is licensed under the MIT License. See the [LICENSE](https://chatgpt.com/c/LICENSE.md) file for details.

## 🙏 Acknowledgments
- Android Developers Documentation
- Jetpack Compose and Material 3 Teams
- Philipp Lackner for Compose tutorials and Clean Architecture guidance





