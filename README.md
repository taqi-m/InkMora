<div align="center">
  <img 
    src="https://raw.githubusercontent.com/taqi-m/InkMora/refs/heads/master/assets/banner.jpg" 
    alt="InkMora - AI-Powered Notes with Dynamic Theming"
    width="100%"
    max-width="1200px"
    style="border-radius: 8px; margin-bottom: 24px;"
  />
</div>

# 🖋️ InkMora

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack-Compose-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/Dagger-Hilt-yellow.svg?style=flat&logo=dagger)](https://dagger.dev/hilt/)
[![Room](https://img.shields.io/badge/Room-Persistence-green.svg?style=flat&logo=android)](https://developer.android.com/training/data-storage/room)
[![Gemini](https://img.shields.io/badge/AI-Gemini-orange.svg?style=flat&logo=google-gemini)](https://ai.google.dev/)

> **InkMora** is a high-performance Android notes application that redefines the writing experience through **Semantic Design Tokens**. Using on-device and cloud-based LLMs, the app's visual identity evolves in real-time, matching the mood and context of your thoughts.

---

### 🚀 Key Features

*   **`🧠 AI Theming`** — Describe a vibe or write a note; the app dynamically generates Material 3 palettes and typography styles.
*   **`🛡️ Accessibility Guard`** — Automated WCAG contrast validation for every AI-generated theme.
*   **`⚡ Clean CRUD`** — Lightning-fast note management with full offline support via Room DB.
*   **`🔍 Semantic Search`** — Search that understands context, not just keywords.

---

### 🛠️ Tech Stack

| Category | Component | Chip |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.3 (K2 Compiler) | `Kotlin` |
| **UI Framework** | Jetpack Compose | `Compose` |
| **Architecture** | Clean Architecture + MVVM | `Solid-Architecture` |
| **DI** | Dagger Hilt | `Hilt` |
| **Database** | Room + KSP Processing | `Room` |
| **AI Engine** | Gemini Android SDK | `Gemini-Pro/Nano` |

---

### 🏗️ Architecture Overview

InkMora follows **Clean Architecture** principles to ensure strict separation of concerns, testability, and scalability:

1.  **`Data Layer`** — Handles Room persistence, Gemini API networking, and DataStore state management.
2.  **`Domain Layer`** — The core logic. Contains UseCases, Repository interfaces, and Domain models.
3.  **`Presentation Layer`** — Modern Jetpack Compose UI driven by ViewModels and state-aware components.

---

### 🏁 Getting Started

1.  **Clone & Open**: Open the project in **Android Studio Ladybug** (or later).
2.  **API Key**: Add your Gemini API key to your `local.properties`.
3.  **Build**: Run `./gradlew assembleDebug` to trigger KSP and Hilt annotation processing.
4.  **Launch**: Deploy to any physical device or emulator running **Android 7.1 (API 25)** or higher.

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
