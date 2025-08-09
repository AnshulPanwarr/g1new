# 🏆 GameOn - Ultimate Sports Connection App

<div align="center">
  <img src="app/src/main/res/basketball_image.jpeg" alt="GameOn Logo" width="200"/>
  
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
  [![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
  [![License](https://img.shields.io/badge/License-MIT-red.svg)](LICENSE)
  
  **Connect. Play. Compete.**
</div>

## 📱 About GameOn

GameOn is your ultimate sports connection app that brings sports lovers together and makes playing your favorite games easier, more social, and more exciting than ever. Whether you're looking to organize a friendly badminton match, join a local cricket team, or participate in tournaments, GameOn has you covered.

Our mission is simple: **connect players, coaches, and talent in one vibrant sports community**.

## ✨ Features

### 🏟️ Core Functionality
- **🎯 Sport Selection**: Choose from 10+ popular sports (Cricket, Football, Basketball, Tennis, etc.)
- **👥 Team Management**: Create or join teams with skill-based matching
- **💬 Real-time Chat**: Instant messaging within teams with file sharing
- **🏆 Tournament System**: Organize and participate in local tournaments
- **📍 Location Services**: Find nearby players, teams, and venues using Google Maps
- **👤 User Profiles**: Comprehensive profiles with stats and achievements

### 🚀 Advanced Features
- **🔍 Smart Discovery**: AI-powered recommendations for teams and players
- **📊 Performance Analytics**: Track your sports journey with detailed statistics
- **💰 Prize Pool Management**: Monetary rewards and sponsorship opportunities
- **📅 Event Scheduling**: Automated match scheduling and reminders
- **🎖️ Achievement System**: Badges, rankings, and milestone tracking
- **💳 In-app Payments**: Secure booking for venues and tournament registrations

### 💎 Subscription Plans
- **Free**: Basic team joining and chat
- **Basic** (₹99/month): Tournament creation, advanced stats
- **Premium** (₹299/month): Prize pools, sponsorship, analytics, priority support

## 🛠️ Tech Stack

### Frontend
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI
- **Material 3** - Latest Material Design
- **Navigation Compose** - Type-safe navigation

### Backend & Services
- **Firebase Authentication** - Secure user management
- **Firestore Database** - Real-time data storage
- **Firebase Storage** - Media file handling
- **Firebase Analytics** - User behavior insights
- **Firebase Messaging** - Push notifications
- **Google Maps API** - Location services
- **Google Play Billing** - In-app purchases

### Development Tools
- **Android Studio** - IDE
- **Gradle** - Build system
- **Git** - Version control

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio** (Arctic Fox or later)
- **JDK 11** or higher
- **Android SDK** (API level 24+)
- **Git**
- **Google account** (for Firebase setup)

## 🚀 Getting Started

### 1. Fork & Clone the Repository

```bash
# Fork this repository on GitHub first, then clone your fork
git clone https://github.com/YOUR_USERNAME/g1new.git
cd g1new
```

### 2. Firebase Setup

Since this project uses Firebase, you'll need to set up your own Firebase project:

1. **Create a Firebase Project**:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Click "Create a project"
   - Follow the setup wizard

2. **Enable Required Services**:
   - **Authentication** → Enable Email/Password
   - **Firestore Database** → Create in test mode
   - **Storage** → Set up with default rules
   - **Analytics** → Enable (optional)

3. **Add Android App**:
   - Click "Add app" → Android
   - Package name: `eu.tutorials.g1`
   - Download `google-services.json`
   - Place it in `app/` directory (replace existing one)

4. **Configure Google Maps** (Optional):
   - Get API key from [Google Cloud Console](https://console.cloud.google.com/)
   - Add it to `local.properties`:
     ```
     MAPS_API_KEY=your_api_key_here
     ```

### 3. Open in Android Studio

1. **Open Android Studio**
2. **File** → **Open** → Select the `g1new` directory
3. **Wait for Gradle sync** to complete
4. **Build** → **Make Project** to ensure everything compiles

### 4. Run the App

1. **Connect an Android device** or **start an emulator**
2. **Click Run** (▶️) or press `Shift + F10`
3. **App should launch** with the login screen

## 📱 App Architecture

```
app/src/main/java/eu/tutorials/g1/
├── model/              # Data models
│   ├── Team.kt
│   ├── Tournament.kt
│   ├── Sport.kt
│   └── Subscription.kt
├── ui/theme/           # UI themes and styling
├── MainActivity.kt     # Entry point
├── AppNavigation.kt    # Navigation setup
├── Screen.kt          # Screen definitions
├── BottomBar.kt       # Bottom navigation
├── LoginScreen.kt     # Authentication
├── GameSport.kt       # Sport selection
├── CreateTeam.kt      # Team creation
├── ChatRoom.kt        # Real-time chat
├── Profile.kt         # User profiles
└── ...                # Other screens
```

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### 🐛 Reporting Bugs
1. Check existing [Issues](https://github.com/YOUR_USERNAME/g1new/issues)
2. Create a new issue with:
   - Clear description
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots (if applicable)

### 💡 Suggesting Features
1. Open a [Feature Request](https://github.com/YOUR_USERNAME/g1new/issues/new)
2. Describe the feature and its benefits
3. Include mockups or examples if possible

### 🔧 Code Contributions

1. **Fork the repository**
2. **Create a feature branch**:
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Follow coding standards**:
   - Use Kotlin naming conventions
   - Add comments for complex logic
   - Write meaningful commit messages
5. **Test your changes**
6. **Commit and push**:
   ```bash
   git commit -m "Add amazing feature"
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

### 📝 Coding Guidelines
- **Language**: Kotlin preferred
- **UI**: Use Jetpack Compose
- **Architecture**: Follow MVVM pattern
- **Naming**: Use descriptive variable/function names
- **Comments**: Document complex logic
- **Testing**: Add unit tests for new features

## 🐛 Known Issues & Solutions

### Common Setup Issues

1. **Firebase Connection Error**:
   - Ensure `google-services.json` is in the correct location
   - Check package name matches Firebase configuration

2. **Gradle Sync Failed**:
   - Try **File** → **Invalidate Caches and Restart**
   - Check internet connection

3. **Maps Not Loading**:
   - Verify Google Maps API key is correct
   - Ensure Maps SDK is enabled in Google Cloud Console

4. **Build Errors**:
   - Clean and rebuild: **Build** → **Clean Project** → **Rebuild Project**
   - Check Android SDK is properly installed

## 📚 Resources & Documentation

- [Android Development Guide](https://developer.android.com/guide)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Android Documentation](https://firebase.google.com/docs/android/setup)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

## 🔮 Roadmap

### 🎯 Upcoming Features
- [ ] **Video Highlights**: Share match highlights
- [ ] **Coach Booking**: Connect with professional coaches
- [ ] **Live Streaming**: Stream matches in real-time
- [ ] **AI Match Predictions**: Smart game analysis
- [ ] **Social Feed**: Instagram-style sports posts
- [ ] **Wearable Integration**: Connect fitness trackers

### 🚧 Current Development
- [ ] Tournament bracket system
- [ ] Advanced profile editing
- [ ] Payment gateway integration
- [ ] Push notification improvements

## 📞 Support & Contact

- **Email**: ap499466@gmail.com
- **WhatsApp Community**: [Join Here](https://chat.whatsapp.com/LkYLg0FLmUN3yQagw2G7g7?mode=ac_t)
- **GitHub Issues**: [Report Here](https://github.com/YOUR_USERNAME/g1new/issues)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Firebase** for backend services
- **Google Maps** for location services
- **Material Design** for UI components
- **Android Community** for continuous support
- **Contributors** who help improve this project

---

<div align="center">
  <p>Made with ❤️ for the sports community</p>
  <p>⭐ Star this repo if you found it helpful!</p>
</div>
