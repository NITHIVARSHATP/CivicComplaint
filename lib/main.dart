import 'package:flutter/material.dart';
// This import assumes you have a file named 'login_screen.dart' inside a 'screens' folder.
// Make sure you have created this file and the LoginScreen widget within it.
import 'screens/login_screen.dart';

// The main function is the entry point for all Flutter apps.
void main() {
  runApp(const MyApp());
}

// MyApp is the root widget of your application.
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Civic Complaint System',
      // The theme defines the visual appearance of your app.
      theme: ThemeData(
        // The color scheme is based on a seed color, which automatically
        // generates a harmonious palette.
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF4A148C), // A deep, professional purple
          primary: const Color(0xFF4A148C),
          secondary: const Color(0xFFF50057), // A vibrant pink for accents and buttons
          background: const Color(0xFFF5F5F5), // A light grey for screen backgrounds
        ),
        useMaterial3: true,

        // Define a consistent theme for all AppBars in the app.
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF4A148C), // Primary purple color
          foregroundColor: Colors.white, // Text and icon color
          elevation: 4,
          centerTitle: true,
          titleTextStyle: TextStyle(
            fontSize: 20,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
        ),

        // Define a consistent theme for all ElevatedButtons.
        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ElevatedButton.styleFrom(
            backgroundColor: const Color(0xFF4A148C), // Primary purple color
            foregroundColor: Colors.white, // Text and icon color
            padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8),
            ),
            textStyle: const TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),

        // Define a consistent look for all text input fields.
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          filled: true,
          fillColor: Colors.white70,
        ),
      ),
      // This hides the debug banner in the top-right corner.
      debugShowCheckedModeBanner: false,
      // The 'home' property sets the first screen that is displayed.
      home: const LoginScreen(),
    );
  }
}

