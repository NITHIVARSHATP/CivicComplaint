import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:civic_management_system/models/complaint_model.dart';

class OfficialDashboardApi {
  // default base URL points to Android emulator host; change to your real server as needed
  final String baseUrl;

  OfficialDashboardApi({this.baseUrl = 'http://10.0.2.2:8080/api'});

  /// Fetch complaints assigned to the official.
  /// Adjust the endpoint path to match your backend API.
  Future<List<Complaint>> getComplaintsForOfficial() async {
    final uri = Uri.parse('$baseUrl/complaints/assigned'); // change path if your API differs

    final response = await http.get(uri);

    if (response.statusCode == 200) {
      try {
        final body = json.decode(response.body);

        // Expecting a JSON array of complaint objects.
        if (body is List) {
          return body
              .map<Complaint>((e) => Complaint.fromJson(e as Map<String, dynamic>))
              .toList();
        } else {
          // If API wraps results, adapt accordingly (e.g., body['data']).
          throw Exception('Unexpected response format: expected a JSON array.');
        }
      } catch (e) {
        throw Exception('Failed to parse complaints: $e');
      }
    } else {
      throw Exception('Failed to load complaints: ${response.statusCode} ${response.reasonPhrase}');
    }
  }

  /// Example helper to fetch complaints for a specific official id:
  Future<List<Complaint>> getComplaintsForOfficialId(int officialId) async {
    final uri = Uri.parse('$baseUrl/officials/$officialId/complaints');
    final response = await http.get(uri);

    if (response.statusCode == 200) {
      final body = json.decode(response.body);
      if (body is List) {
        return body.map((e) => Complaint.fromJson(e as Map<String, dynamic>)).toList();
      } else {
        throw Exception('Unexpected response format.');
      }
    } else {
      throw Exception('Failed to load complaints for official $officialId');
    }
  }
}
