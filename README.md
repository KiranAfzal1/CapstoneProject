# Social App - Social Learning App (Demo Android App)

This is an Android application built as a learning project.  
It combines quizzes, tasks, study tools, and chat features into a single platform.  
The app also integrates **Firebase** for authentication and **Google AdMob** for ads.

---

## ✨ Features

1. **Quiz Module**
   - 5 MCQs with 10-second timer each  
   - Automatic question skip after timer runs out  
   - Final result screen with score and percentage
   - Previous score screen 
   - Navigation: Home → Quiz → Result  
   - Clean and modern UI using CardView and XML  

2. **Task Management**
   - Task creation, editing, deletion, and completion tracking  
   - Room Database for local task storage  
   - Firestore Cloud Storage for remote task management  

3. **Authentication**
   - Firebase Authentication for login/registration  
   - SharedPreferences to manage login state  
   - Email format validation & minimum 6-character password requirement  

4. **Chat & Communication**
   - Real-time messaging with Firestore/Realtime Database  
   - User chat list management (stores name & email)  
   - Shows real-time user online/offline status 

5. **Ads Integration**
   - Banner ads on all onboarding screens  
   - Interstitial ad after the Policy screen 

6. **Other**
   - Proper error handling (no crashes on invalid input)  
   - Follows MVVM Architecture for clean code  

---

## ⚠️ Notice about `google-services.json`

- This project includes a **google-services.json** file only for demo/testing purposes.  
- The Firebase project linked to this file is **not used in production**.  
- No real or sensitive user data is stored or processed.  
- No billing is enabled on the Firebase project.  
- If you plan to make a similar app, please generate and use your own **google-services.json** file by creating a Firebase project.  
- Do **not reuse** the included Firebase config in production apps.  

---

## 🚀 How to Run

1. Download or clone this repository:  
   git clone https://github.com/KiranAfzal1/CapstoneProject.git
2. Open the files on android studio
3. Run the app on an emulator or your physical device

---

## Sceenshots
