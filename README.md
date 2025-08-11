
# 📝 NotesApp

A modern and clean note-taking Android app built using 
**Jetpack Compose**,
**MVVM**,
**Clean Architecture**,
and **Hilt DI**. 
It allows users to create, edit, delete, and organize notes with custom colors.


---

## 🚀 Features

- ✅ Add, Edit, Delete Notes  
- 🎨 Choose random background colors  
- 🔍 Sort notes by **Title**, **Date**, or **Color**
- 📥 Undo deleted notes  
- 🧱 Built with **Clean Architecture** (data, domain, presentation layers)
- 💉 Dependency Injection with **Hilt**
- 🧪 Unit & UI Testing (JUnit5, MockK, Espresso, Compose UI Testing)
- 🧪 Integrated with **GitHub Actions CI**

---

## 🧑‍💻 Tech Stack

| Layer         | Tools Used                              |
|---------------|------------------------------------------|
| UI            | Jetpack Compose, Navigation, Material3   |
| Architecture  | MVVM + Clean Architecture                |
| Dependency Injection | Hilt                               |
| DB            | Room                                     |
| Language      | Kotlin                                   |
| Testing       | JUnit5, MockK, Compose Testing, Espresso |
| CI/CD         | GitHub Actions                           |

---

## 🧪 Run Tests

### ✅ Unit Tests & Instrumented UI Tests

```bash
./gradlew testDebugUnitTest

./gradlew connectedDebugAndroidTest

Ensure an emulator is running or use reactivecircus/android-emulator-runner in CI.
```




