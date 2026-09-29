<p align="center">
  <img src="docs/screenshots/smartmailbox-logo.svg" alt="SmartMailbox" width="80%">
</p>
<p align="center">
  An Android app for managing SmartMailboxes.
</p>

<div align="center">
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
  <img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white">
  <img alt="Jetpack Compose" src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white">
  <img alt="Material 3" src="https://img.shields.io/badge/Material_3-757575?style=for-the-badge&logo=materialdesign&logoColor=white">
  <img alt="Firebase" src="https://img.shields.io/badge/Firebase-DD2C00?style=for-the-badge&logo=firebase&logoColor=white">
  <img alt="Google Sign-In" src="https://img.shields.io/badge/Google_Sign--In-4285F4?style=for-the-badge&logo=google&logoColor=white">
  <img alt="Retrofit" src="https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge&logo=square&logoColor=white">
  <img alt="CameraX" src="https://img.shields.io/badge/CameraX-34A853?style=for-the-badge&logo=android&logoColor=white">
  <img alt="ZXing" src="https://img.shields.io/badge/ZXing_QR-000000?style=for-the-badge&logo=qrcode&logoColor=white">
  <img alt="Gradle" src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
</div>

<br>

<div align="center">
  <img src="docs/screenshots/home-with-mailbox.png" alt="Home" width="22%">
  <img src="docs/screenshots/details.png" alt="Mailbox unlocked" width="22%">
  <img src="docs/screenshots/details-activity.png" alt="Activity" width="22%">
  <img src="docs/screenshots/scan-qr-code.png" alt="Scan QR code" width="22%">
</div>

---

## 📖 About

An Android app built with Kotlin and Jetpack Compose for managing SmartMailbox devices.

Backstory: this started as a university project that didn't turn out as planned. After the course ended, I kept building on it:
- 🔄 Redesigned the app and its structure and added more features
- 🔥 Moved login and data to **Firebase Auth** and **Firestore**; the original ran on a local-only Express.js (MERN) backend


## ✨ Features

- 🔐 **Login** and **Create Account** with **Firebase Auth**
- ➕ Add a device with its unique verification code (would come with the device) and give it a name
- 📬 Manage your SmartMailbox
  - Unlock it when someone is there to deliver something
  - Lock it if no one is there yet or something is wrong
  - Auto-lock after the door closes
  - View its activity: who did what, and when
  - Manage its settings
  - Manage access with roles
    - **Admin** – all access
    - **Manager** – manage access, unlock, view activity
    - **Member** – unlock, view activity
    - **Visitor** – unlock only (e.g. a neighbor)
  - **Lockdown** – block all access when there's trouble or something valuable is inside
- 🔔 Notifications
  - Mailbox activity (unlocked, opened, locked, lockdown...)
  - Changes to your access
  - Group invites
- 📷 Scan a device's QR code to open it if you have access (e.g. a delivery person), using the **Direct4me sandbox API**
- 👤 Profile
  - Edit profile
  - Notification settings
  - Logout
- 📊 Activity – see all activity across your mailboxes, with filters

<details>
<summary>📸 All screenshots</summary>
<br>

<table align="center">
  <tr>
    <td align="center"><b>Create account</b><br><img src="docs/screenshots/create-account.png" width="250"></td>
    <td align="center"><b>Log in</b><br><img src="docs/screenshots/log-in.png" width="250"></td>
    <td align="center"><b>Home (empty)</b><br><img src="docs/screenshots/home-no-mailboxes.png" width="250"></td>
  </tr>
  <tr>
    <td align="center"><b>Add mailbox: code</b><br><img src="docs/screenshots/add-mailbox-verification.png" width="250"></td>
    <td align="center"><b>Add mailbox: name</b><br><img src="docs/screenshots/add-mailbox-name.png" width="250"></td>
    <td align="center"><b>Home</b><br><img src="docs/screenshots/home-with-mailbox.png" width="250"></td>
  </tr>
  <tr>
    <td align="center"><b>Mailbox</b><br><img src="docs/screenshots/details.png" width="250"></td>
    <td align="center"><b>Lockdown</b><br><img src="docs/screenshots/details-lockdown.png" width="250"></td>
    <td align="center"><b>Unlocked</b><br><img src="docs/screenshots/details-unlocked.png" width="250"></td>
  </tr>
  <tr>
    <td align="center"><b>Door open</b><br><img src="docs/screenshots/details-door-open.png" width="250"></td>
    <td align="center"><b>Activity</b><br><img src="docs/screenshots/details-activity.png" width="250"></td>
    <td align="center"><b>Scan QR code</b><br><img src="docs/screenshots/scan-qr-code.png" width="250"></td>
  </tr>
  <tr>
    <td align="center"><b>Unlock with sound</b><br><img src="docs/screenshots/unlock-with-sound.png" width="250"></td>
    <td align="center"><b>Inbox</b><br><img src="docs/screenshots/inbox.png" width="250"></td>
    <td align="center"><b>Profile</b><br><img src="docs/screenshots/profile.png" width="250"></td>
  </tr>
</table>

</details>

## 💻 Tech Stack

- 🟣 Kotlin + Jetpack Compose
- 🔥 Firebase Auth (email + Google Sign-In)
- 🗄️ Cloud Firestore
- 🌐 Retrofit + Gson for the **Direct4me sandbox API**
- 📷 CameraX + ZXing for QR code scanning

## 📋 Requirements

### 📱 To use the app
- Android phone running **Android 8.0 or newer**
- Google Play Services (preinstalled on most Android phones)
- Internet connection
- Optional:
  - A SmartMailbox device with verification code or at least a generated verification code (you can also open shared mailboxes with QR code)

### 🛠️ To build and develop
- Android Studio Narwhal 3 Feature Drop (2025.1.3) or newer
- Android SDK 36 (install it in Android Studio's SDK Manager if Gradle asks for it)
- JDK 11 or newer (bundled with Android Studio)
- Android device or emulator running **Android 8.0 (API 26) or newer**
  - The app uses `java.time` (`Instant`) for mailbox activity timestamps, which is only available from API 26
- A Firebase project with **Authentication** and **Firestore** turned on
  - Place its `google-services.json` in `SmartMailBox/app/`

## 🚀 Install & Run

1. Clone the project
   ```bash
   git clone https://github.com/Maercel/smart-mailbox-android-v2.git
   ```
2. Create a Firebase project, turn on **Authentication** (Email and Google) and **Firestore**
3. Download `google-services.json` and put it in `SmartMailBox/app/`
4. *(Optional):* Add test devices to Firestore with [`seed-devices.js`](SmartMailBox/tools/seed-devices/)
5. Open the `SmartMailBox` folder in Android Studio and wait for Gradle to sync
6. Select your device or emulator and press **Run** (<kbd>Shift</kbd> + <kbd>F10</kbd>)

## 📁 Project Structure

The app is organized by feature (feature-based):

- `data/` – Firestore and API access (repositories, DTOs)
- `domain/` – models and business logic
- `ui/` – Compose screens, ViewModels and UI state

```
com.example.smartmailbox
├── auth/              # Login, register, Google sign-in
├── home/              # Your mailboxes
├── addmailbox/        # Add a device with its verification code and name it
├── mailbox/           # Scan QR code and open mailbox
├── mailboxdetail/     # Mailbox details (unlock, lock, activity, access...)
├── mailboxactivity/   # Activity history of current mailbox
├── mailboxaccess/     # Roles and access management of current mailbox
├── mailboxsettings/   # Mailbox settings of current mailbox
├── mailboxhelp/       # Help of current mailbox
├── activity/          # All activity, with filters
├── inbox/             # Notifications
├── profile/           # Profile, logout
├── navigation/        # Navigation between screens
├── api/               # Retrofit client and local data (old auth data too)
├── ui/                # Theme and shared components
└── App.kt             # App entry and navigation graph
```

## 🧪 Tests

Unit tests (JUnit) are in `SmartMailBox/app/src/test`. Run them with:

```bash
cd SmartMailBox
./gradlew test
```

Or in Android Studio: right-click the `test` folder → **Run Tests**.

---

## 🤝 Credits

- 🎓 Started as a university project with [@S1monPet](https://github.com/S1monPet)
- 📦 QR unlock via the [Direct4me](https://direct4.me) sandbox API
