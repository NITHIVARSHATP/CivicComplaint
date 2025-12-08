import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:civic_management_system/models/complaint_model.dart'; // Ensure this model exists
import 'package:civic_management_system/config/global_config.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CitizenDashboardApi {

  static final String baseUrl = GlobalConfig.baseUrl;

  // Base path defined in Controller: @RequestMapping("/api/complaints")
  static final String apiPath = "$baseUrl/complaints";

  CitizenDashboardApi();

  /// Create a new complaint.
  /// Endpoint: POST /api/complaints/add
  /// Create a new complaint.
  /// If complaintData doesn't contain 'submittedByUserId', this method will
  /// attach the stored userId from SharedPreferences (key: 'userId').
  Future<Complaint> createComplaint(Map<String, dynamic> complaintData) async {
    // 1) Attach stored userId when not present
    try {
      final prefs = await SharedPreferences.getInstance();
      final storedUserId = prefs.getString('userId')?.toString() ?? "";

      final hasSubmittedById = complaintData.containsKey('submittedByUserId') &&
          complaintData['submittedByUserId'] != null &&
          complaintData['submittedByUserId'].toString().isNotEmpty;

      if (!hasSubmittedById && storedUserId.isNotEmpty) {
        complaintData['submittedByUserId'] = storedUserId;
      }
    } catch (e) {
      // If SharedPreferences fails, continue — caller may have provided id or backend may derive it.
      print('Warning: failed to read stored userId: $e');
    }

    // 2) Prepare request
    final uri = Uri.parse('$apiPath/add');

    try {
      final response = await http.post(
        uri,
        headers: {"Content-Type": "application/json"},
        body: jsonEncode(complaintData),
      );

      // Debug logs
      print('CREATE COMPLAINT -> status: ${response.statusCode}');
      print('CREATE COMPLAINT -> body: ${response.body}');

      if (response.statusCode == 201 || response.statusCode == 200) {
        final body = json.decode(response.body);
        return Complaint.fromJson(body as Map<String, dynamic>);
      } else {
        // raise clearer exception including server body
        throw Exception('Failed to create complaint: ${response.statusCode} - ${response.body}');
      }
    } catch (e) {
      throw Exception('Error creating complaint: $e');
    }
  }


  /// Get all complaints.
  /// Endpoint: GET /api/complaints
  /// Note: The provided controller returns ALL complaints.
  /// If you need to filter by citizen, you must currently do it here in Dart
  /// or add a filter endpoint in Spring Boot.
  Future<List<Complaint>> getAllComplaints() async {
    final uri = Uri.parse('$apiPath/fetch');

    try {
      final response = await http.get(uri);

      if (response.statusCode == 200) {
        final body = json.decode(response.body);
        print('fetch -> status: ${response.statusCode}');
        print('fetch -> body: ${response.body}');
        try {
          final parsed = jsonDecode(response.body);
          if (parsed is Map && parsed['error'] != null) {
            print('LOGIN -> server error message: ${parsed['error']}');
          }
        } catch (_) {}
        if (body is List) {
          return body
              .map<Complaint>((e) => Complaint.fromJson(e as Map<String, dynamic>))
              .toList();
        } else {
          throw Exception('Unexpected response format: expected a JSON array.');
        }
      } else {
        throw Exception('Failed to load complaints: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Failed to fetch complaints: $e');
    }
  }

  /// Get a single complaint by ID.
  /// Endpoint: GET /api/complaints/{id}
  Future<List<Complaint>> getComplaintById(int userId) async {
    final uri = Uri.parse('$apiPath/user/$userId'); // matches /api/complaints/user/{userId}

    try {
      final response = await http.get(uri);

      if (response.statusCode == 200) {
        final List<dynamic> body = json.decode(response.body);
        return body.map((json) => Complaint.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load complaints: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error fetching complaints: $e');
    }
  }


  // --- Optional: Methods usually reserved for Officials/Admins,
  // but included here if this API file is shared ---

  /// Update status (e.g. RESOLVED).
  /// Endpoint: PATCH /api/complaints/{id}/status?status=...
  Future<void> updateStatus(int id, String statusEnum) async {
    final uri = Uri.parse('$apiPath/$id/status').replace(queryParameters: {
      'status': statusEnum,
    });

    final response = await http.patch(uri);

    if (response.statusCode != 200) {
      throw Exception('Failed to update status: ${response.statusCode}');
    }
  }
}