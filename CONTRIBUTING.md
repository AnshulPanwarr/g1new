# 🤝 Contributing to GameOn

Thank you for your interest in contributing to GameOn! We welcome contributions from developers of all skill levels. This guide will help you get started.

## 📋 Table of Contents
1. [How to Contribute](#how-to-contribute)
2. [Setting Up Development Environment](#setting-up-development-environment)
3. [Project Structure](#project-structure)
4. [Coding Standards](#coding-standards)
5. [Submitting Changes](#submitting-changes)
6. [Issue Guidelines](#issue-guidelines)
7. [Pull Request Process](#pull-request-process)
8. [Community Guidelines](#community-guidelines)

## 🚀 How to Contribute

### Types of Contributions We Welcome
- 🐛 **Bug fixes**
- ✨ **New features**
- 📚 **Documentation improvements**
- 🎨 **UI/UX enhancements**
- ⚡ **Performance optimizations**
- 🧪 **Tests and test coverage**
- 🌐 **Translations and localization**

## 🛠️ Setting Up Development Environment

### Prerequisites
- **Android Studio** (Arctic Fox or later)
- **JDK 11** or higher
- **Android SDK** (API level 24+)
- **Git**
- **Google account** (for Firebase)

### Step-by-Step Setup

#### 1. Fork and Clone
```bash
# Fork the repository on GitHub first
git clone https://github.com/YOUR_USERNAME/g1new.git
cd g1new
```

#### 2. Set Up Firebase
Since GameOn uses Firebase, you'll need your own Firebase project:

1. **Create Firebase Project**:
   - Visit [Firebase Console](https://console.firebase.google.com/)
   - Click "Create a project"
   - Name it "GameOn-Dev" (or similar)

2. **Enable Services**:
   - **Authentication** → Email/Password
   - **Firestore Database** → Test mode
   - **Storage** → Default rules
   - **Analytics** (optional)

3. **Add Android App**:
   - Package name: `eu.tutorials.g1`
   - Download `google-services.json`
   - Replace `app/google-services.json`

#### 3. Configure Google Maps (Optional)
```bash
# Add to local.properties
echo "MAPS_API_KEY=your_google_maps_api_key" >> local.properties
```

#### 4. Open in Android Studio
1. **File** → **Open** → Select `g1new` directory
2. Wait for Gradle sync
3. **Build** → **Make Project**

#### 5. Test Your Setup
```bash
# Run the app
./gradlew assembleDebug
```

## 📁 Project Structure

```
app/src/main/java/eu/tutorials/g1/
├── model/              # Data models (Team, Tournament, etc.)
├── ui/theme/           # UI themes and styling
├── MainActivity.kt     # App entry point
├── AppNavigation.kt    # Navigation setup
├── Screen.kt          # Screen definitions
├── *Screen.kt         # Individual screens
└── *.kt               # Other components
```

### Key Files to Know
- **`AppNavigation.kt`**: Main navigation logic
- **`Screen.kt`**: Screen route definitions
- **`model/`**: Data structures
- **`BottomBar.kt`**: Bottom navigation
- **Firebase integration**: Most screens use Firebase

## 📝 Coding Standards

### Kotlin Style Guide
Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

#### Naming Conventions
```kotlin
// Classes: PascalCase
class TeamManager

// Functions and variables: camelCase
fun createTeam()
val teamName = "Warriors"

// Constants: UPPER_SNAKE_CASE
const val MAX_TEAM_SIZE = 10

// Composables: PascalCase
@Composable
fun TeamCard()
```

#### Code Organization
```kotlin
// File structure
package declaration
imports (grouped: Android, third-party, project)
class/object declaration
```

#### Comments
```kotlin
// Use meaningful comments for complex logic
/**
 * Creates a new team with the specified parameters
 * @param name The team name
 * @param sport The sport type
 * @return Team ID on success
 */
fun createTeam(name: String, sport: String): String?
```

### UI Guidelines

#### Jetpack Compose Best Practices
```kotlin
// Use remember for state
var teamName by remember { mutableStateOf("") }

// Extract complex composables
@Composable
fun TeamList() {
    LazyColumn {
        items(teams) { team ->
            TeamCard(team = team)
        }
    }
}

// Use meaningful parameter names
@Composable
fun TeamCard(
    team: Team,
    onTeamClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

#### Material Design 3
- Use **Material 3** components
- Follow **Material Design** guidelines
- Maintain **consistent spacing** (8dp grid)
- Use **semantic colors** from theme

## 🔧 Firebase Integration

### Authentication
```kotlin
val auth = FirebaseAuth.getInstance()
val currentUser = auth.currentUser
```

### Firestore
```kotlin
val db = FirebaseFirestore.getInstance()
// Use collection paths consistently
val teamsRef = db.collection("teams")
```

### Storage
```kotlin
val storage = FirebaseStorage.getInstance()
val imageRef = storage.reference.child("images/${UUID.randomUUID()}")
```

## 📋 Issue Guidelines

### Before Creating an Issue
1. **Search existing issues** to avoid duplicates
2. **Check if it's already fixed** in the latest version
3. **Gather information**: steps to reproduce, screenshots, device info

### Bug Reports
Include:
- **Clear title** describing the problem
- **Steps to reproduce** the issue
- **Expected behavior** vs **actual behavior**
- **Screenshots** or **screen recordings**
- **Device information** (Android version, device model)
- **App version** or commit hash

### Feature Requests
Include:
- **Clear description** of the feature
- **Use case** and **benefits**
- **Mockups** or **examples** if applicable
- **Implementation suggestions** (optional)

### Issue Labels
- 🐛 `bug` - Something isn't working
- ✨ `enhancement` - New feature or request
- 📚 `documentation` - Documentation improvements
- 🆘 `help wanted` - Extra attention is needed
- 🥇 `good first issue` - Good for newcomers

## 🔄 Pull Request Process

### Before Submitting

1. **Create a branch** for your changes:
   ```bash
   git checkout -b feature/team-chat-improvements
   ```

2. **Make your changes** following coding standards

3. **Test thoroughly**:
   - App builds successfully
   - Features work as expected
   - No regressions introduced

4. **Update documentation** if needed

### Pull Request Checklist

- [ ] **Descriptive title** and description
- [ ] **Linked to relevant issue** (if applicable)
- [ ] **Code follows** project conventions
- [ ] **App builds** without errors
- [ ] **Features tested** on device/emulator
- [ ] **Screenshots** included for UI changes
- [ ] **Documentation updated** (if needed)

### PR Description Template
```markdown
## 📝 Description
Brief description of changes

## 🔗 Related Issue
Fixes #123

## 🧪 Testing
- [ ] Tested on Android device
- [ ] Tested on emulator
- [ ] All features work as expected

## 📱 Screenshots
(Include screenshots for UI changes)

## ✅ Checklist
- [ ] Code follows project style
- [ ] Self-review completed
- [ ] Documentation updated
```

### Review Process
1. **Automated checks** must pass
2. **Code review** by maintainers
3. **Testing** on different devices
4. **Approval** and merge

## 🌟 Development Tips

### Common Tasks

#### Adding a New Screen
1. Create screen file: `NewScreen.kt`
2. Add to `Screen.kt` sealed class
3. Add route to `AppNavigation.kt`
4. Update bottom bar if needed

#### Firebase Operations
```kotlin
// Always handle errors
db.collection("teams")
    .add(team)
    .addOnSuccessListener { /* success */ }
    .addOnFailureListener { error ->
        Log.e("Firebase", "Error adding team", error)
        // Show user-friendly error message
    }
```

#### State Management
```kotlin
// Use proper state handling
@Composable
fun TeamScreen() {
    var teams by remember { mutableStateOf<List<Team>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        // Load teams
    }
}
```

### Debugging
- **Use meaningful log tags**: `Log.d("TeamManager", "Loading teams")`
- **Firebase debugging**: Enable debug logging
- **Network debugging**: Use Firebase console
- **UI debugging**: Use Layout Inspector

## 🎯 Contribution Areas

### High Priority
- [ ] **Tournament bracket system**
- [ ] **Advanced profile editing**
- [ ] **Push notification improvements**
- [ ] **Performance optimizations**

### Medium Priority
- [ ] **UI/UX improvements**
- [ ] **Code documentation**
- [ ] **Unit tests**
- [ ] **Accessibility improvements**

### Good First Issues
- [ ] **String externalization**
- [ ] **Icon updates**
- [ ] **Minor UI fixes**
- [ ] **Documentation improvements**

## 👥 Community Guidelines

### Code of Conduct
- **Be respectful** and inclusive
- **Help others** learn and grow
- **Provide constructive** feedback
- **Follow** [GitHub Community Guidelines](https://docs.github.com/en/github/site-policy/github-community-guidelines)

### Communication
- **Use English** for all communication
- **Be clear and specific** in issues and PRs
- **Ask questions** if you need help
- **Share knowledge** with other contributors

### Recognition
Contributors will be:
- **Listed** in project acknowledgments
- **Credited** in release notes
- **Invited** to join the core team (for significant contributions)

## 📞 Getting Help

### Where to Ask Questions
- **GitHub Issues**: Technical problems
- **WhatsApp Group**: [Join Here](https://chat.whatsapp.com/LkYLg0FLmUN3yQagw2G7g7?mode=ac_t)
- **Email**: ap499466@gmail.com

### Resources
- [Android Development Guide](https://developer.android.com/guide)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs)

---

**Thank you for contributing to GameOn! Together, we're building the ultimate sports community app. 🏆** 