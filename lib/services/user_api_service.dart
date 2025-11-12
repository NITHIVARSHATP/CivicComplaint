import 'dart:io';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:path/path.dart'; // Import for basename
import 'package:http_parser/http_parser.dart'; // Import for MediaType

class UserApiService {
  // --- IMPORTANT ---
  // Replace this with your actual backend server URL
  // 10.0.2.2 is the special address for Android Emulator to access host's localhost
  static const String _baseUrl = "http://10.0.2.2:8080/api/users";

  /// Logs in a user.
  /// Sends email and password as JSON and expects a JSON response.
  static Future<http.Response> loginUser({
    required String email,
    required String password,
  }) async {
    final Uri loginUri = Uri.parse("$_baseUrl/login");

    final String body = jsonEncode({
      'email': email,
      'password': password,
    });

    try {
      final http.Response response = await http.post(
        loginUri,
        headers: {"Content-Type": "application/json"},
        body: body,
      ).timeout(const Duration(seconds: 10)); // Added timeout

      return response;
    } catch (e) {
      // Catch network errors, timeouts, etc.
      // Return a custom response or rethrow
      return http.Response('Error: ${e.toString()}', 500);
    }
  }

  /// Registers a user without a photo (JSON only).
  static Future<http.Response> registerUserJson({
    required String fullName,
    required String email,
    required String password,
    required String role,
  }) async {
    final Uri registerUri = Uri.parse("$_baseUrl/register");

    final String body = jsonEncode({
      'fullName': fullName,
      'email': email,
      'password': password,
      'role': role,
    });

    try {
      final http.Response response = await http.post(
        registerUri,
        headers: {"Content-Type": "application/json"},
        body: body,
      ).timeout(const Duration(seconds: 10)); // Added timeout

      return response;
    } catch (e) {
      return http.Response('Error: ${e.toString()}', 500);
    }
  }

  /// Registers a user with a photo (Multipart request).
  static Future<http.Response> registerUserWithPhoto({
    required String fullName,
    required String email,
    required String password,
    required String role,
    required File photoFile,
  }) async {
    final Uri registerUri = Uri.parse("$_baseUrl/register/photo");

    try {
      // Create a multipart request
      final http.MultipartRequest request = http.MultipartRequest('POST', registerUri);

      // Add text fields
      request.fields['fullName'] = fullName;
      request.fields['email'] = email;
      request.fields['password'] = password;
      request.fields['role'] = role;

      // Add the file
      final http.MultipartFile photo = await http.MultipartFile.fromPath(
        'photo', // This 'photo' key must match your backend's expected field name
        photoFile.path,
        filename: basename(photoFile.path), // Uses path package
        contentType: MediaType('image', 'jpeg'), // Example: set content type
      );
      request.files.add(photo);

      // Send the request and get the response
      final http.StreamedResponse streamedResponse = await request.send().timeout(const Duration(seconds: 30)); // Added timeout

      // Convert StreamedResponse to http.Response
      final http.Response response = await http.Response.fromStream(streamedResponse);

      return response;

    } catch (e) {
      return http.Response('Error: ${e.toString()}', 500);
    }
  }
}