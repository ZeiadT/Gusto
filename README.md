<h1 align="center">
  <img src="app/src/main/res/mipmap-hdpi/ic_launcher_round.webp" alt="Gusto Logo" width="80"/>
  <br/>
  Gusto 🍽️
</h1>

<p align="center">
  <b>Cook with Joy</b> — A modern Android recipe app that helps you discover meals, manage your favourites, and plan your weekly menu.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Language-Java-f89820?logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue" />
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue" />
  <img src="https://img.shields.io/badge/Architecture-MVP%20%2B%20Clean-6A0DAD" />
  <img src="https://img.shields.io/badge/License-MIT-green" />
</p>

---

## 📲 Download

> **Get the latest release directly on your Android device:**

👉 [**Download from GitHub Releases**](../../releases/latest)

1. Download the `.apk` from the **Releases** section
2. Enable **Install from Unknown Sources** on your device
3. Open the downloaded file and install

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔍 **Discover** | Browse a curated feed of recipes — featured meal of the day, popular categories, and countries |
| 🔎 **Smart Search** | Search meals by name or filter by tag chips (category / country) |
| ❤️ **Favourites** | Save any meal to your personal favourites list with a single tap, with a satisfying ripple wave animation |
| 📅 **Meal Planner** | Schedule meals by date and meal type (Breakfast / Lunch / Dinner) with a bottom-sheet picker |
| 🍳 **Meal Details** | Full recipe screen with hero image, category & country chips, embedded YouTube video player, expandable ingredients list, and step-by-step instructions |
| 🔐 **Authentication** | Email/password sign-up & login, Google Sign-In, or continue as Guest |
| 🌗 **Theme Support** | Light / Dark / System Default theme, persisted across sessions |
| 🌍 **Localization** | Multi-language support managed at runtime |
| 📴 **Offline Handling** | Animated no-connection screen with auto-reconnect when network is restored |
| ⚙️ **Settings** | Theme switcher, language picker, and account options |

---

## 🏗️ Architecture

Gusto follows **MVP (Model-View-Presenter)** with a **Clean Architecture** layering:

```
├── core/               # App-wide managers (Network, Theme, Localization, Vibration, Preferences)
├── data/               # Retrofit REST client, Room DAO, Firebase sources, Repositories
├── domain/             # Plain entity models & mappers (no Android dependencies)
└── presentation/       # Activities, Fragments, Adapters, Presenters, Contracts
    ├── auth/           # Splash, Onboarding, Login, Register
    ├── main/           # Discover, Search, Favourites, Plan, Settings (Navigation Component)
    └── mealdetails/    # Meal detail screen
```

- **Repository pattern** decouples data sources (Remote API, Room DB, Firebase Firestore)
- **RxJava 3** handles all async data streams between layers
- **Contracts** (interface pairs) enforce a strict View ↔ Presenter boundary

---

## 🛠️ Tech Stack

### Language & Platform
| | |
|---|---|
| Language | Java 11 |
| Platform | Android (Min SDK 24 · Target SDK 36) |
| Build System | Gradle with Version Catalogs (`libs.versions.toml`) |

### UI & Navigation
| Library | Version | Purpose |
|---|---|---|
| Material Components | 1.11.0 | Chips, Cards, FAB, Bottom Sheets |
| ConstraintLayout | 2.1.4 | Responsive layouts |
| RecyclerView | 1.4.0 | Lists and grids |
| Navigation Component | 2.9.7 | Single-Activity navigation with Safe Args |
| Lottie | 6.7.1 | Animated illustrations (no-connection, loading) |
| MotionToast | 1.4 | Themed in-app toast notifications |
| AndroidYouTubePlayer | 13.0.0 | Embedded YouTube video playback |

### Data & Networking
| Library | Version | Purpose |
|---|---|---|
| Retrofit 2 | 3.0.0 | REST API client (TheMealDB) |
| Gson | 2.13.2 | JSON parsing |
| Room | 2.8.4 | Local SQLite database (favourites, plan) |
| RxJava 3 | 3.1.5 | Reactive async programming |
| RxAndroid | 3.0.2 | Android schedulers for RxJava |
| RxJava3 Retrofit Adapter | 3.0.0 | Retrofit ↔ RxJava bridge |

### Image Loading
| Library | Version | Purpose |
|---|---|---|
| Glide | 4.16.0 | Network image loading & caching |
| Palette | 1.0.0 | Dynamic colour extraction from images |

### Firebase & Auth
| Library | Version | Purpose |
|---|---|---|
| Firebase BOM | 34.8.0 | Dependency management |
| Firebase Auth | — | Email/password authentication |
| Firebase Firestore | — | Cloud storage for user favourites & plan |
| Firebase Analytics | — | Usage analytics |
| Google Sign-In / Credentials API | 21.5.0 / 1.5.0 | One-tap Google authentication |

---

## 🔌 Data Source

Gusto is powered by the free **[TheMealDB API](https://www.themealdb.com/api.php)**:
- Meal search by name
- Meal details (ingredients, instructions, YouTube link)
- Browse by category & area/country

---

## 🚀 Getting Started (Build from Source)

### Prerequisites
- Android Studio **Meerkat** or newer
- JDK 11+
- A `google-services.json` from your own Firebase project placed in `app/`

### Steps

```bash
# Clone the repository
git clone https://github.com/<your-username>/Gusto.git

# Open in Android Studio, sync Gradle, then run on a device or emulator
```

> **Note:** Without a valid `google-services.json` the Firebase features (Auth, Firestore) will not work. You can obtain one by creating a project at [console.firebase.google.com](https://console.firebase.google.com).

---

## 📁 Project Structure

```
Gustozo/
├── app/src/main/java/iti/mad/gusto/
│   ├── core/                   # Managers: Network, Theme, Localization, SharedPrefs, Room
│   ├── data/
│   │   ├── model/              # API response DTOs
│   │   ├── repo/               # Repositories (Meal, Auth, Favourites, Plan, Settings)
│   │   ├── service/            # Retrofit API interface (TheMealDB)
│   │   └── source/             # Remote (API), Local (Room), Firebase sources
│   ├── domain/
│   │   ├── entity/             # Pure entities (MealEntity, IngredientEntity, …)
│   │   └── mapper/             # DTO → Entity mappers
│   └── presentation/
│       ├── auth/               # Splash → Onboarding → Login / Register
│       ├── common/             # Shared utilities, custom Views, components
│       ├── main/               # Main navigation host + fragments
│       │   ├── discover/       # Home feed
│       │   ├── search/         # Search + tag filters
│       │   ├── favourite/      # Saved meals
│       │   ├── plan/           # Weekly meal plan
│       │   └── settings/       # Theme & language
│       └── mealdetails/        # Full recipe screen
└── app/src/main/res/           # Layouts, drawables, fonts, animations, strings
```

---

## 🤝 Contributing

Pull requests are welcome! For major changes please open an issue first to discuss what you would like to change.

---

## 📄 License

```
MIT License — feel free to use, modify, and distribute.
```
