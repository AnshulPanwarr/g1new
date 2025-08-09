# 🚀 GameOn Setup Guide

This guide will help you set up GameOn on your local development environment. Follow these steps carefully to get the app running on your machine.

## 📋 Prerequisites

Before starting, make sure you have:

### Required Software
- **Android Studio** (Arctic Fox 2020.3.1 or later)
- **JDK 11** or higher
- **Git** for version control
- **Google Account** (for Firebase services)

### System Requirements
- **RAM**: 8GB minimum, 16GB recommended
- **Storage**: 10GB free space
- **OS**: Windows 10+, macOS 10.14+, or Linux

## 🔧 Step 1: Android Studio Setup

### 1.1 Download Android Studio
- Visit [Android Studio Download Page](https://developer.android.com/studio)
- Download the latest stable version
- Install following the setup wizard

### 1.2 Configure Android SDK
1. Open Android Studio
2. Go to **Tools** → **SDK Manager**
3. Install these SDK components:
   - **Android 14 (API 34)** - Latest
   - **Android 7.0 (API 24)** - Minimum required
   - **Android SDK Build-Tools 34.0.0**
   - **Google Play Services**

### 1.3 Setup Emulator (Optional)
1. Go to **Tools** → **AVD Manager**
2. Create new virtual device:
   - **Device**: Pixel 6 or similar
   - **API Level**: 31 or higher
   - **RAM**: 2GB minimum

## 📁 Step 2: Project Setup

### 2.1 Fork the Repository
1. Go to [GameOn GitHub Repository](https://github.com/YOUR_USERNAME/g1new)
2. Click **Fork** button (top right)
3. This creates a copy in your GitHub account

### 2.2 Clone Your Fork
```bash
# Clone your forked repository
git clone https://github.com/YOUR_USERNAME/g1new.git

# Navigate to project directory
cd g1new

# Add upstream remote (to sync with original repo)
git remote add upstream https://github.com/ORIGINAL_USERNAME/g1new.git
```

### 2.3 Open in Android Studio
1. **File** → **Open**
2. Navigate to `g1new` folder
3. Click **OK**
4. Wait for **Gradle sync** to complete (this may take several minutes)

## 🔥 Step 3: Firebase Configuration

Since GameOn uses Firebase for backend services, you need to set up your own Firebase project.

### 3.1 Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **Create a project**
3. Project name: `GameOn-Development` (or similar)
4. **Continue** through setup wizard
5. Choose your **Google Analytics** preference

### 3.2 Enable Required Services

#### Authentication
1. In Firebase Console, go to **Authentication**
2. Click **Get started**
3. Go to **Sign-in method** tab
4. Enable **Email/Password**
5. Click **Save**

#### Firestore Database
1. Go to **Firestore Database**
2. Click **Create database**
3. Choose **Start in test mode**
4. Select a location close to you
5. Click **Done**

#### Storage
1. Go to **Storage**
2. Click **Get started**
3. Choose **Start in test mode**
4. Select same location as Firestore
5. Click **Done**

#### Analytics (Optional)
1. Go to **Analytics**
2. Click **Enable Google Analytics**

### 3.3 Add Android App to Firebase
1. In Firebase Console, click **Project Overview**
2. Click **Add app** → **Android**
3. Fill in app details:
   - **Package name**: `eu.tutorials.g1`
   - **App nickname**: `GameOn Development`
   - **Debug signing certificate**: Leave empty for now
4. Click **Register app**
5. **Download** `google-services.json`
6. **Important**: Replace the existing `app/google-services.json` with your downloaded file

### 3.4 Firestore Security Rules (Development)
For development, use these basic rules:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow read/write for authenticated users
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## 🗺️ Step 4: Google Maps Setup (Optional)

For location features to work:

### 4.1 Get Google Maps API Key
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your Firebase project or create new one
3. Go to **APIs & Services** → **Credentials**
4. Click **Create Credentials** → **API Key**
5. Copy the API key

### 4.2 Enable Required APIs
1. Go to **APIs & Services** → **Library**
2. Search and enable:
   - **Maps SDK for Android**
   - **Places API**
   - **Geocoding API**

### 4.3 Configure API Key
1. In your project, open/create `local.properties`
2. Add this line:
   ```properties
   MAPS_API_KEY=your_actual_api_key_here
   ```
3. Save the file

**Note**: Never commit API keys to version control!

## 🔨 Step 5: Build and Run

### 5.1 Initial Build
```bash
# Clean and build the project
./gradlew clean build
```

### 5.2 Run the App
1. **Connect Android device** (enable USB debugging) OR **start emulator**
2. In Android Studio, click **Run** (▶️) or press `Shift + F10`
3. Select your device/emulator
4. App should install and launch

### 5.3 Verify Setup
If everything is configured correctly:
- App launches with login screen
- You can create a new account
- Firebase authentication works
- Basic navigation functions

## 🐛 Troubleshooting

### Common Issues and Solutions

#### Gradle Sync Failed
```bash
# Try these commands in order:
./gradlew clean
./gradlew --refresh-dependencies
```
Or in Android Studio: **File** → **Invalidate Caches and Restart**

#### Firebase Connection Issues
- Verify `google-services.json` is in `app/` directory
- Check package name matches: `eu.tutorials.g1`
- Ensure Firebase services are enabled

#### Maps Not Loading
- Verify API key in `local.properties`
- Check if Maps SDK is enabled in Google Cloud Console
- Ensure location permissions are granted

#### Build Errors
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDebug
```

#### App Crashes on Launch
- Check Android logs: **View** → **Tool Windows** → **Logcat**
- Look for Firebase initialization errors
- Verify all dependencies are correctly installed

### Getting Help
If you're still having issues:
1. Check [GitHub Issues](https://github.com/YOUR_USERNAME/g1new/issues)
2. Join our [WhatsApp Group](https://chat.whatsapp.com/LkYLg0FLmUN3yQagw2G7g7?mode=ac_t)
3. Email: ap499466@gmail.com

## 🎯 Next Steps

Once your setup is complete:

1. **Explore the Code**: Start with `MainActivity.kt` and `AppNavigation.kt`
2. **Read Documentation**: Check `CONTRIBUTING.md` for coding guidelines
3. **Find Issues**: Look for `good first issue` labels on GitHub
4. **Join Community**: Connect with other developers

## 📱 Testing Your Setup

Create a simple test to verify everything works:

1. **Launch the app**
2. **Sign up** with a test email
3. **Select a sport** (e.g., Cricket)
4. **Create a team** with basic details
5. **Open team chat** and send a message

If all these steps work, your setup is complete! 🎉

## 🔄 Staying Updated

To keep your fork updated with the main repository:

```bash
# Fetch latest changes from upstream
git fetch upstream

# Switch to main branch
git checkout main

# Merge upstream changes
git merge upstream/main

# Push updated main to your fork
git push origin main
```

## 📚 Additional Resources

- [Android Development Guide](https://developer.android.com/guide)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Firebase Android Documentation](https://firebase.google.com/docs/android/setup)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)

---

**Welcome to GameOn development! Happy coding! 🏆** 