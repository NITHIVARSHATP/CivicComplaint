import 'package:flutter/material.dart';
import 'package:civic_management_system/models/complaint_model.dart';
import 'package:civic_management_system/services/citizen_dashboard_api.dart';
import 'package:civic_management_system/screens/add_complaint_screen.dart';
import 'package:civic_management_system/complaint_card.dart';
import 'package:civic_management_system/screens/login_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CitizenDashboardScreen extends StatefulWidget {
  const CitizenDashboardScreen({super.key});

  @override
  State<CitizenDashboardScreen> createState() => _CitizenDashboardScreenState();
}

class _CitizenDashboardScreenState extends State<CitizenDashboardScreen> {
  late Future<List<Complaint>> _complaintsFuture;
  final CitizenDashboardApi _apiService = CitizenDashboardApi();

  @override
  void initState() {
    super.initState();
    _loadComplaints();
  }

  void _loadComplaints() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final storedUserId = prefs.getString('userId');

      if (storedUserId == null) {
        print("User ID not found in SharedPreferences");
        return;
      }

      final userId = int.parse(storedUserId); // Convert to int

      // Call the correct API (fetch all complaints of user)
      setState(() {
        _complaintsFuture = _apiService.getComplaintById(userId);
      });
    } catch (e) {
      print("Error loading complaints: $e");
    }
  }


  void _navigateAndRefresh() async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const AddComplaintScreen()),
    );
    setState(() {
      _loadComplaints();
    });
  }

  @override
  Widget build(BuildContext context) {
    const primaryBlue = Color(0xFF1976D2);
    const primaryGreen = Color(0xFF2E7D32);
    const background = Color(0xFFF5F5F5);

    return Scaffold(
      backgroundColor: background,
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        centerTitle: true,
        iconTheme: const IconThemeData(color: Colors.black87),
        title: const Text(
          'My Complaints',
          style: TextStyle(
            color: Colors.black87,
            fontWeight: FontWeight.w600,
          ),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () {
              Navigator.pushAndRemoveUntil(
                context,
                MaterialPageRoute(builder: (_) => const LoginScreen()),
                    (route) => false,
              );
            },
          ),
        ],
      ),
      body: Column(
        children: [
          const SizedBox(height: 8),
          // Brand header – like the register screen
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 4),
            child: Column(
              children: [
                Container(
                  height: 52,
                  width: 52,
                  decoration: BoxDecoration(
                    color: primaryBlue.withOpacity(0.08),
                    borderRadius: BorderRadius.circular(16),
                  ),
                  child: const Icon(
                    Icons.apartment,
                    size: 30,
                  ),
                ),
                const SizedBox(height: 8),
                RichText(
                  text: const TextSpan(
                    children: [
                      TextSpan(
                        text: 'Civic',
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.w700,
                          color: primaryBlue,
                        ),
                      ),
                      TextSpan(
                        text: 'Connect',
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.w700,
                          color: primaryGreen,
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 12),
              ],
            ),
          ),

          // Card holding the complaint list / states
          Expanded(
            child: Padding(
              padding:
              const EdgeInsets.symmetric(horizontal: 16.0, vertical: 4.0),
              child: Card(
                elevation: 2,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(18),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: FutureBuilder<List<Complaint>>(
                    future: _complaintsFuture,
                    builder: (context, snapshot) {
                      if (snapshot.connectionState ==
                          ConnectionState.waiting) {
                        return const Center(child: CircularProgressIndicator());
                      } else if (snapshot.hasError) {
                        return _buildStateMessage(
                          context,
                          icon: Icons.error_outline,
                          title: 'Something went wrong',
                          message: 'Error: ${snapshot.error}',
                        );
                      } else if (!snapshot.hasData ||
                          snapshot.data!.isEmpty) {
                        return _buildStateMessage(
                          context,
                          icon: Icons.report_gmailerrorred_outlined,
                          title: 'No complaints yet',
                          message:
                          'You have not filed any complaints yet.\nTap "New Complaint" to get started.',
                        );
                      }

                      final complaints = snapshot.data!;
                      return RefreshIndicator(
                        onRefresh: () async {
                          setState(() {
                            _loadComplaints();
                          });
                          await _complaintsFuture;
                        },
                        child: ListView.builder(
                          physics: const AlwaysScrollableScrollPhysics(),
                          itemCount: complaints.length,
                          itemBuilder: (context, index) {
                            return Padding(
                              padding: const EdgeInsets.symmetric(
                                  horizontal: 4.0, vertical: 4.0),
                              child: ComplaintCard(complaint: complaints[index]),
                            );
                          },
                        ),
                      );
                    },
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _navigateAndRefresh,
        backgroundColor: primaryGreen,
        icon: const Icon(Icons.add),
        label: const Text('New Complaint'),
      ),
    );
  }

  Widget _buildStateMessage(
      BuildContext context, {
        required IconData icon,
        required String title,
        required String message,
      }) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, size: 48, color: Colors.grey.shade500),
            const SizedBox(height: 12),
            Text(
              title,
              style: Theme.of(context).textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.w600,
              ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 6),
            Text(
              message,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                color: Colors.grey.shade600,
                height: 1.4,
              ),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}
