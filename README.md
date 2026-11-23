# Cookify — Android Recipe Discovery & Meal Planner (Kotlin)

Native Android Kotlin application for discovering international recipes via TheMealDB REST API, curating favorites, and scheduling daily meal plans inside an offline-capable Room SQLite database.

## Features
- **Account Authentication & Demo Access**: Local Room user persistence (`UserDao`, `SessionManager`) plus one-tap **Try Demo Chef Account** (`demo@cookify.tn` / `Demo1234!`).
- **Recipe Discovery (`MainActivity`)**: Live Retrofit integration with TheMealDB (`searchMeals`, `getMealById`) supporting dynamic search queries and Glide image loading.
- **Recipe Details & Favorites (`RecipeDetailActivity`, `FavoritesActivity`)**: Step-by-step preparation instructions, ingredients parser, and persistent Room favorites pre-seeded on first launch.
- **Weekly Meal Calendar (`CalendarActivity`, `DayPlanActivity`, `MealPickerActivity`)**: Interactive monthly calendar with breakfast/lunch/dinner slot assignment, tasting notes, and daily notification reminders (`ReminderReceiver`).

## Project Structure
```
Cookify-Android-Recipe-Meal-Planner/
├── app/src/main/
│   ├── java/tn/poly/cookify/
│   │   ├── activities/         # Login, Signup, Main, Detail, Favorites, Calendar, DayPlan, MealPicker
│   │   ├── adapters/           # RecipeAdapter & FavoriteAdapter
│   │   ├── api/                # RetrofitClient & ApiService (TheMealDB)
│   │   ├── database/           # AppDatabase (Room + demo seed callback) & DAOs
│   │   ├── models/             # Recipe, FavoriteRecipe, MealPlan, User
│   │   ├── receivers/          # ReminderReceiver
│   │   └── utils/              # SessionManager & NotificationHelper
│   └── res/                    # Culinary Amber Material 3 layouts, drawables & themes
└── docs/
    ├── Rapport_du_Projet_Koitlin_Cookify.pdf
    ├── koitlin project explanation.pdf
    ├── projet__examen_android.pdf
    └── screenshots/
```

## Quick Start

1. Open the repository root in **Android Studio** (or build from CLI with Android SDK & JDK 17):
   ```bash
   ./gradlew assembleDebug
   ```
2. Install on an emulator or device and tap **Try Demo Chef Account** on the login screen to explore pre-seeded favorites and calendar meal plans.
