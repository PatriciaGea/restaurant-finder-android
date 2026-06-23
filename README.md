# Restaurant Finder App `![Restaurant List](screenshots/android_logo.png)`

> A Android app for finding restaurants, built with Jetpack Compose and modern Android architecture.

---

##  ✦ Screenshots


> `![Restaurant List](screenshots/screenshot.png)`


> `![App Demo](screenshots/demo.gif)`

---

##  ✦ Project Objective

Build a mobile application for **Munchies** a Umain's new restaurant service, allowing users to:

- Browse restaurants fetched from a live REST API
- Filter restaurants by category using horizontal filter chips
- View restaurant details including open/closed status in real time

---

##  ✦ Tech Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary language |
| **Jetpack Compose** | Declarative UI framework |
| **Retrofit 2** | HTTP client for REST API calls |
| **Kotlinx Serialization** | JSON parsing |
| **OkHttp Logging Interceptor** | Network request debugging |
| **Coil** | Async image loading from URLs |
| **Navigation Compose** | Screen navigation (list → detail) |
| **ViewModel + StateFlow** | UI state management |
| **Kotlin Coroutines** | Asynchronous programming |
| **Material3** | UI components and theming |

---

##  ✦ Architecture

The project follows **MVVM (Model-View-ViewModel)** architecture with a clean separation of concerns:

```
app/
├── data/
│   ├── Models.kt               # Data classes (Restaurant, Filter, OpenStatus)
│   ├── RestaurantApi.kt        # Retrofit API interface
│   ├── NetworkModule.kt        # Retrofit singleton configuration
│   └── RestaurantRepository.kt # Data layer abstraction
└── ui/
    ├── list/
    │   ├── RestaurantListScreen.kt    # List UI + Filter chips
    │   └── RestaurantListViewModel.kt # List state management
    └── detail/
        ├── RestaurantDetailScreen.kt    # Detail UI
        └── RestaurantDetailViewModel.kt # Open/closed status fetching
```

---

##  ✦ Features

- **Restaurant List** = Displays all restaurants fetched from the API with image, name, tags, delivery time and rating
- **Horizontal Filter Chips** = Filter icons and names fetched from the API (not hardcoded). Multiple filters can be selected simultaneously
- **Real-time Open/Closed Status** = Each restaurant's status is fetched live when opening the detail screen
- **Detail Screen** = Full-screen banner image, restaurant name, category tags and open/closed status
- **Error Handling** = Graceful error states with user-friendly messages
- **Loading States** = Visual feedback while data is being fetched

---

##  ✦ API

Base URL: `https://food-delivery.umain.io/api/v1/`

| Endpoint | Description |
|---|---|
| `GET /restaurants` | Fetch all restaurants |
| `GET /filter/{id}` | Fetch filter details (name, icon) |
| `GET /open/{id}` | Fetch open/closed status for a restaurant |

---

##  ✦ Design

The UI is based on a **Figma design** provided by Umain, implementing:

- Custom color palette
- Pixel-accurate card dimensions (`343 x 196dp`) with Material3 elevation
- Custom fonts: **Inter** (rating) and **Poppins** (filter chips)
- Gradient header separator
- Umain logo rendered as a Vector Drawable

---

##  ✦ How to Run
### Prerequisites

- Android Studio **Hedgehog** or later
- JDK 11+
- Android SDK **API 37**
- Internet connection (app fetches live data)

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/PatriciaGea/restaurant-finder-android.git

# 2. Open in Android Studio
# File → Open → select the cloned folder

# 3. Let Gradle sync finish automatically

# 4. Run on emulator or physical device
# Press the ▶️ Run button or Shift + F10
```

>  **Minimum SDK:** API 24 (Android 7.0)
>  **Target SDK:** API 36

---

##  ✦ Concepts Practiced

- **Jetpack Compose** declarative UI with `LazyColumn`, `LazyRow`, and `Box` layouts
- **StateFlow** for reactive UI state, the screen automatically redraws when data changes
- **Coroutines** with `viewModelScope` for safe async operations tied to the UI lifecycle
- **Parallel API calls** using `async`/`awaitAll` to fetch multiple filters simultaneously
- **Navigation Compose** with JSON-encoded arguments to pass objects between screens
- **MVVM pattern** with clean separation between data, business logic and UI layers
- **Retrofit + Kotlinx Serialization** for type-safe API consumption
- **Coil** for efficient image loading and caching from remote URLs
- **Material3 components** (`Card`, `CardDefaults`) for professional elevation and shadows
- **Vector Drawables** for scalable logo rendering at any screen density

---

## ✦ Challenges & Learnings

### Parallel Filter Fetching
The API returns `filterIds` inside each restaurant, but filter details (name, icon) require separate calls. The solution was to collect all unique filter IDs across all restaurants and fetch them **in parallel** using Kotlin's `async`/`awaitAll`, reducing load time significantly compared to sequential calls.

### Navigation with Complex Objects
Passing a `Restaurant` object and its `List<Filter>` between screens required serializing them to JSON using Kotlinx Serialization and URL-encoding them as navigation arguments, a pattern that mirrors real-world production approaches.

### Dependency Compatibility
The project uses `compileSdk = 37` to satisfy `androidx.core:core-ktx:1.19.0` requirements, while keeping `targetSdk = 36` for runtime behavior stability. The Retrofit Kotlinx Serialization converter was migrated from the deprecated `com.jakewharton.retrofit` package to the official `com.squareup.retrofit2:converter-kotlinx-serialization`.

---
##  ✦ Author

**Patrícia Gea Rodrigues** • Android Developer

#### https://patriciageadev.vercel.app/
#### https://www.linkedin.com/in/patriciageadev/
#### patricia.gea@gmail.com

---


*Built as part of the Umain Mobile internship Test*