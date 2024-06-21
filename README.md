# HealMe
HealMe is an AI-driven mobile application designed to reduce healthcare disparities by providing early skin disease detection. The app leverages advanced machine learning to analyze skin images, offering immediate guidance and locating nearby healthcare facilities for users to get immediate assistance from healthcare professionals. Our goal is to enhance health outcomes and improve diagnostic accessibility and efficiency, particularly for vulnerable populations.

## Tech Stack

- **Retrofit**: A type-safe HTTP client for Android and Java to interact with API services.
- **Datastore**: A local data storage solution for managing user preferences.
- **Room Database**: An abstraction layer over SQLite used for storing scan history and other user data.
- **Google Play Services**: Libraries used for integrating Google APIs, including the "Get Nearby healthcare" feature for finding nearby healthcare facilities.
- **Android Jetpack Libraries**: A collection of libraries to help in building robust Android apps, including components for lifecycle management, layout design, and more.

## Features

### User Management

- **Sign Up**: Allows new users to create an account.
- **Login**: Enables existing users to access their accounts.
- **Update Password**: Provides users with the ability to change their passwords.
- **Get Profile**: Retrieves user profile information.
- **Update Profile**: Allows users to update their profile details.

### Predictions Management

- **Get All Predictions**: Fetches a list of all skin disease predictions made by the user.
- **Create a Prediction**: Allows users to detect skin wounds by selecting an image from the gallery or camera for disease prediction.
- **Get a Single Prediction**: Retrieves details of a specific prediction.

### Additional Features

- **Get Nearby (Nearby Places API)**: Uses the Nearby Places API to find nearby medical facilities or dermatologists based on the user's location.
- **Generative Model**: Uses a generative model for displaying explanations and first-aid recommendations based on the results. Additionally, it provides health tips.

## Installation

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- Minimum SDK version 21

### Clone the Repository

- Open your terminal.
- Change the current working directory to the location where you want the cloned directory.
- Copy the following command and paste it into the terminal:
```sh
git clone https://github.com/healme-app/mobile.git
cd mobile
```

### Install an Existing Release

- Go to the [Releases page](https://github.com/healme-app/mobile/releases) of the repository.
- Find the latest release and download the APK file (eg., 'HealMe.apk').
- On your Android device, you may need to allow your device to install from unknown sources. Enable `Install from Unknown Sources` to allow the installation of apps from sources other than the Google Play Store.
- Tap on the APK file to start the installation process.
- Follow the on-screen instructions to complete the installation.

This `README.md` provides clear instructions on how to clone the repository and install an existing release to get started with the latest version of the application. 
Thank you for using HealMe. Together, we can make healthcare more accessible and efficient for everyone.
