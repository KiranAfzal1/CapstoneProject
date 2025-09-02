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

### Splash Screen 
![WhatsApp Image 2025-09-02 at 21 29 25_2773a183](https://github.com/user-attachments/assets/f22457f1-cb2f-4655-8208-fee912047bee)

## Auth Screens:
### Login
![WhatsApp Image 2025-09-02 at 21 32 18_bff75a40](https://github.com/user-attachments/assets/2d842824-1c68-4a8f-87f0-04f17b2ab1c5)
### Register
![WhatsApp Image 2025-09-02 at 21 33 16_46ff0a7a](https://github.com/user-attachments/assets/1f2a6caf-794d-48ed-8d0f-a38cfece2268)

## Onboarding Screens:
### Welcome
![WhatsApp Image 2025-09-02 at 21 33 51_de0d9c47](https://github.com/user-attachments/assets/f16ea994-1a6b-4e4d-96e8-c31b8d241f34)
### Features
![WhatsApp Image 2025-09-02 at 21 34 22_48984c73](https://github.com/user-attachments/assets/30dbbf4e-83d7-44d8-b277-6e092d425360)
### PrivacyPolicy
![WhatsApp Image 2025-09-02 at 21 34 40_3e720ce8](https://github.com/user-attachments/assets/4980a706-c027-472a-be9f-7be6187a2c6a)

## MainActivity
![WhatsApp Image 2025-09-02 at 21 34 59_391ce381](https://github.com/user-attachments/assets/f4edce77-db26-4cc2-bed5-e92b09aa98a3)
## Setting
![WhatsApp Image 2025-09-02 at 21 35 23_b6c8d8db](https://github.com/user-attachments/assets/09120390-6582-4bf8-ac13-4422e96820fd)

## Chat Screens:
### ChatGroupList
![WhatsApp Image 2025-09-02 at 21 35 53_fdf3b95b](https://github.com/user-attachments/assets/08f94b35-a29d-44db-9cd2-d44f786dbac3)
### ChatActivity
![WhatsApp Image 2025-09-02 at 21 36 13_5d687476](https://github.com/user-attachments/assets/c370c281-111e-4fc3-900a-4c389109ddc8)
### ProfileActivity
![WhatsApp Image 2025-09-02 at 21 36 47_59374525](https://github.com/user-attachments/assets/4cf5fa67-6be8-4afd-b5b5-ebeb0ef6ddcc)

## Quiz Screens:
### Quiz Home
![WhatsApp Image 2025-09-02 at 21 37 29_8f0c2b9a](https://github.com/user-attachments/assets/4cb98c3f-279e-47cc-8a96-20ebc850714f)
### Quiz Question
![WhatsApp Image 2025-09-02 at 21 37 30_d2323c46](https://github.com/user-attachments/assets/bb0318b5-b129-45ce-b338-366678843926)
### Quiz Result
![WhatsApp Image 2025-09-02 at 21 37 30_b19be3f0](https://github.com/user-attachments/assets/25063085-c25f-49b9-b6ec-1aad24692d9b)
### Quiz History
![WhatsApp Image 2025-09-02 at 21 37 29_2aa10ca5](https://github.com/user-attachments/assets/eeb41655-8b8d-49b2-850b-7d0821eea994)

## Task Screens:
### TaskActivity
![WhatsApp Image 2025-09-02 at 21 38 36_33516b4d](https://github.com/user-attachments/assets/43974911-e804-407b-a44e-12bf866eec99)
![WhatsApp Image 2025-09-02 at 21 39 23_c293d265](https://github.com/user-attachments/assets/ecc8ae3b-76b2-4668-a6b4-340a87b1cc22)
### AddEditTaskActivity
![WhatsApp Image 2025-09-02 at 21 39 39_ecc7fd2b](https://github.com/user-attachments/assets/ebf9301e-1aef-4a8b-88b8-2de9aa16f91b)
![WhatsApp Image 2025-09-02 at 21 39 54_a22fb1b2](https://github.com/user-attachments/assets/a33fec0e-8f4b-468a-923c-a03a190dade9)
