import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:civic_management_system/models/complaint_model.dart';

class ApiService {
  // Replace with your computer's IP address when running the backend locally.
  // Do not use 'localhost' as the Android emulator will not be able to find it.
  static const String _baseUrl = 'http://192.168.1.10:8080/api';

  // Fetches all complaints for the citizen view
  Future<List<Complaint>> getComplaintsForCitizen() async {
    final response = await http.get(Uri.parse('$_baseUrl/citizen/complaints'));

    if (response.statusCode == 200) {
      List<dynamic> body = jsonDecode(response.body);
      return body.map((dynamic item) => Complaint.fromJson(item)).toList();
    } else {
      throw Exception('Failed to load complaints');
    }
  }

  // Fetches unresolved complaints for the official view
  Future<List<Complaint>> getComplaintsForOfficial() async {
    final response = await http.get(Uri.parse('$_baseUrl/official/complaints'));

    if (response.statusCode == 200) {
      List<dynamic> body = jsonDecode(response.body);
      return body.map((dynamic item) => Complaint.fromJson(item)).toList();
    } else {
      throw Exception('Failed to load official tasks');
    }
  }

  // Submits a new complaint to the backend
  Future<Complaint> submitComplaint(Map<String, dynamic> data) async {
    final response = await http.post(
      Uri.parse('$_baseUrl/complaints'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(data),
    );

    if (response.statusCode == 201) { // 201 Created
      return Complaint.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to submit complaint');
    }
  }
}