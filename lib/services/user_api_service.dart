import 'dart:io';
import 'dart:convert';
import 'dart:typed_data'; // For handling byte arrays
import 'package:http/http.dart' as http;
import 'package:path/path.dart'; // Import for basename
import 'package:http_parser/http_parser.dart'; // Import for MediaType
import 'package:civic_management_system/config/global_config.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserApiService {
  static final String baseUrl = GlobalConfig.baseUrl;

  // Base endpoint defined in Controller: @RequestMapping("/api/users")
  static final String userEndpoint = "$baseUrl/users";




  /// Logs in a user.
  /// Endpoint: POST /api/users/login
  static Future<http.Response> loginUser({
    required String email,
    required String password,
  }) async {
    print('LOGIN -> userEndpoint: $userEndpoint');
    final Uri loginUri = Uri.parse("$userEndpoint/login");

    final String body = jsonEncode({
      'email': email,
      'password': password,
    });

    try {
      final http.Response response = await http.post(
        loginUri,
        headers: {"Content-Type": "application/json"},
        body: body,
      ).timeout(const Duration(seconds: 10));
      print('LOGIN -> status: ${response.statusCode}');
      print('LOGIN -> body: ${response.body}');

      try {
        final parsed = jsonDecode(response.body);
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('userId', jsonDecode(response.body)['id'].toString());

        if (parsed is Map && parsed['error'] != null) {
          print('LOGIN -> server error message: ${parsed['error']}');
        }
      } catch (_) {}
      return response;
    } catch (e) {
      return http.Response('Error: ${e.toString()}', 500);
    }
  }

  /// Registers a user without a photo (JSON only).
  /// Endpoint: POST /api/users/register
  static Future<http.Response> registerUserJson({
    required String fullName,
    required String email,
    required String password,
    required String role,
    int? departmentId, // Added to match DTO capabilities
  }) async {
    final Uri registerUri = Uri.parse("$userEndpoint/register");

    final Map<String, dynamic> data = {
      'fullName': fullName,
      'email': email,
      'password': password,
      'role': role,
    };

    if (departmentId != null) {
      data['departmentId'] = departmentId;
    }

    try {
      final http.Response response = await http.post(
        registerUri,
        headers: {"Content-Type": "application/json"},
        body: jsonEncode(data),
      ).timeout(const Duration(seconds: 10));
      print('LOGIN -> status: ${response.statusCode}');
      print('LOGIN -> body: ${response.body}');
      try {
        final parsed = jsonDecode(response.body);
        if (parsed is Map && parsed['error'] != null) {
          print('LOGIN -> server error message: ${parsed['error']}');
        }
      } catch (_) {}
      return response;
    } catch (e) {
      print('Error: ${e.toString()}');
      return http.Response('Error: ${e.toString()}', 500);
    }
  }

  /// Registers a user with optional photo (Multipart request).
  /// Endpoint: POST /api/users/register/photo
  /// Matches Controller: @RequestPart(value = "photo", required = false)
  static Future<http.Response> registerUserWithPhoto({
    required String fullName,
    required String email,
    required String password,
    required String role,
    String? departmentId, // Optional
    File? photoFile,      // Optional
  }) async {
    final Uri registerUri = Uri.parse("$userEndpoint/register/photo");

    try {
      final http.MultipartRequest request = http.MultipartRequest('POST', registerUri);

      // Add text fields
      request.fields['fullName'] = fullName;
      request.fields['email'] = email;
      request.fields['password'] = password;
      request.fields['role'] = role;

      if (departmentId != null && departmentId.isNotEmpty) {
        request.fields['departmentId'] = departmentId;
      }

      // Add the file only if provided
      if (photoFile != null) {
        final http.MultipartFile photo = await http.MultipartFile.fromPath(
          'photo',
          photoFile.path,
          filename: basename(photoFile.path),
          // Note: In production, consider using the 'mime' package to determine exact type
          contentType: MediaType('image', 'jpeg'),
        );
        request.files.add(photo);
      }

      final http.StreamedResponse streamedResponse = await request.send().timeout(const Duration(seconds: 30));
      final http.Response response = await http.Response.fromStream(streamedResponse);

      return response;

    } catch (e) {
      return http.Response('Error: ${e.toString()}', 500);
    }
  }

  /// Get user image bytes.
  /// Endpoint: GET /api/users/{id}/image
  /// Returns raw bytes. UI can use Image.memory(response.bodyBytes).
  static Future<http.Response> getUserImage(int userId) async {
    final Uri imageUri = Uri.parse("$userEndpoint/$userId/image");

    try {
      final http.Response response = await http.get(imageUri).timeout(const Duration(seconds: 10));
      return response;
    } catch (e) {
      return http.Response('Error: ${e.toString()}', 500);
    }
  }

  /// Upload or replace user image.
  /// Endpoint: POST /api/users/{id}/image
  static Future<http.Response> uploadUserImage({
    required int userId,
    required File photoFile,
  }) async {
    final Uri uploadUri = Uri.parse("$userEndpoint/$userId/image");

    try {
      final http.MultipartRequest request = http.MultipartRequest('POST', uploadUri);

      final http.MultipartFile photo = await http.MultipartFile.fromPath(
        'photo',
        photoFile.path,
        filename: basename(photoFile.path),
        contentType: MediaType('image', 'jpeg'),
      );
      request.files.add(photo);

      final http.StreamedResponse streamedResponse = await request.send().timeout(const Duration(seconds: 30));
      final http.Response response = await http.Response.fromStream(streamedResponse);

      return response;
    } catch (e) {
      return http.Response('Error: ${e.toString()}', 500);
    }
  }
}