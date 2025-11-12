import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:civic_management_system/screens/citizen_dashboard_screen.dart';
import 'package:civic_management_system/screens/official_dashboard_screen.dart';
import 'package:civic_management_system/services/user_api_service.dart';
import 'package:civic_management_system/screens/login_screen.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final ImagePicker _picker = ImagePicker();
  XFile? _pickedImage;
  String? _selectedRole;
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _loading = false;

  // Define colors from your logo for consistent branding
  static const Color civicBlue = Color(0xFF0D47A1); // Example blue
  static const Color civicGreen = Color(0xFF4CAF50); // Example green

  Future<void> _onSubmit() async {
    setState(() => _loading = true);

    final fullName = _nameController.text.trim();
    final email = _emailController.text.trim();
    final password = _passwordController.text.trim();
    final role = (_selectedRole == 'Official') ? 'OFFICIAL' : 'CITIZEN';

    if (fullName.isEmpty || email.isEmpty || password.isEmpty || _selectedRole == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please fill all fields and select a role')),
      );
      setState(() => _loading = false);
      return;
    }

    try {
      http.Response response;

      if (_pickedImage != null) {
        response = await UserApiService.registerUserWithPhoto(
          fullName: fullName,
          email: email,
          password: password,
          role: role,
          photoFile: File(_pickedImage!.path),
        );
      } else {
        response = await UserApiService.registerUserJson(
          fullName: fullName,
          email: email,
          password: password,
          role: role,
        );
      }

      if (response.statusCode >= 200 && response.statusCode < 300) {
        ScaffoldMessenger.of(context)
            .showSnackBar(const SnackBar(content: Text('Registration successful!')));

        // Clear controllers after successful registration
        _nameController.clear();
        _emailController.clear();
        _passwordController.clear();
        setState(() {
          _pickedImage = null;
          _selectedRole = null;
        });


        if (role == 'OFFICIAL') {
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (_) => const OfficialDashboardScreen()),
          );
        } else {
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (_) => const CitizenDashboardScreen()),
          );
        }
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: ${response.statusCode} - ${response.body}')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    } finally {
      if (mounted) {
        setState(() => _loading = false);
      }
    }
  }

  Future<void> _pickImage() async {
    try {
      final XFile? image = await _picker.pickImage(
        source: ImageSource.gallery,
        imageQuality: 80,
      );
      if (image != null) {
        setState(() {
          _pickedImage = image;
        });
      }
    } catch (e) {
      debugPrint('Image pick error: $e');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Could not pick image')),
        );
      }
    }
  }

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // Shared border style for all input fields
    final inputBorder = OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: BorderSide(color: Colors.grey.shade400, width: 1.5),
    );
    final focusedInputBorder = OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: const BorderSide(color: civicBlue, width: 2),
    );

    return Scaffold(
      backgroundColor: Colors.white, // Set background to white
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(32.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // --- LOGO PLACEHOLDER ---
              // TODO: Add your logo to assets/images/logo.png and pubspec.yaml
              // Then uncomment the line below and delete the Icon:
              // Image.asset('assets/images/logo.png', height: 100),
              Icon(
                Icons.business_outlined, // Placeholder Icon
                size: 80,
                color: civicBlue,
              ),
              const SizedBox(height: 16),
              // --- TITLE FROM LOGO ---
              RichText(
                text: const TextSpan(
                  style: TextStyle(
                    fontSize: 36,
                    fontWeight: FontWeight.bold,
                    fontFamily: 'Roboto', // Ensure you have this font or change as needed
                  ),
                  children: [
                    TextSpan(
                      text: 'Civic',
                      style: TextStyle(color: civicBlue),
                    ),
                    TextSpan(
                      text: 'Connect',
                      style: TextStyle(color: civicGreen),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 40),

              // --- Name Field ---
              TextField(
                controller: _nameController, // <-- BUG FIX: Added controller
                decoration: InputDecoration(
                  labelText: 'Full Name',
                  border: inputBorder,
                  focusedBorder: focusedInputBorder,
                  prefixIcon: const Icon(Icons.person_outline),
                ),
              ),
              const SizedBox(height: 20),

              // --- Email Field ---
              TextField(
                controller: _emailController, // <-- BUG FIX: Added controller
                decoration: InputDecoration(
                  labelText: 'Email',
                  border: inputBorder,
                  focusedBorder: focusedInputBorder,
                  prefixIcon: const Icon(Icons.email_outlined),
                ),
                keyboardType: TextInputType.emailAddress,
              ),
              const SizedBox(height: 20),

              // --- Password Field ---
              TextField(
                controller: _passwordController, // <-- BUG FIX: Added controller
                decoration: InputDecoration(
                  labelText: 'Password',
                  border: inputBorder,
                  focusedBorder: focusedInputBorder,
                  prefixIcon: const Icon(Icons.lock_outline),
                ),
                obscureText: true,
              ),
              const SizedBox(height: 20),

              // --- Upload Photo ---
              GestureDetector(
                onTap: _pickImage,
                child: Container(
                  height: 60,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.grey.shade400, width: 1.5),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Row(
                    children: [
                      if (_pickedImage != null) ...[
                        ClipRRect(
                          borderRadius: BorderRadius.circular(8),
                          child: Image.file(
                            File(_pickedImage!.path),
                            width: 44,
                            height: 44,
                            fit: BoxFit.cover,
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                            child: Text(
                              _pickedImage!.name.length > 20
                                  ? '${_pickedImage!.name.substring(0, 20)}...'
                                  : _pickedImage!.name,
                              overflow: TextOverflow.ellipsis,
                            )),
                        const Icon(Icons.photo_library_outlined, color: civicGreen),
                      ] else ...[
                        const Icon(Icons.camera_alt_outlined, color: Colors.grey),
                        const SizedBox(width: 12),
                        const Expanded(
                            child: Text('Upload Your Photo (Optional)')),
                        const Icon(Icons.upload_file_outlined, color: Colors.grey),
                      ],
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 20),

              // --- Role dropdown ---
              DropdownButtonFormField<String>(
                value: _selectedRole,
                decoration: InputDecoration(
                  labelText: 'Select your role',
                  border: inputBorder,
                  focusedBorder: focusedInputBorder,
                  prefixIcon: const Icon(Icons.group_outlined),
                ),
                items: const [
                  DropdownMenuItem(value: 'Citizen', child: Text('Citizen')),
                  DropdownMenuItem(value: 'Official', child: Text('Official')),
                ],
                onChanged: (v) => setState(() => _selectedRole = v),
                hint: const Text('Select role'),
              ),
              const SizedBox(height: 30),

              // --- Register Button ---
              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton(
                  onPressed: _loading ? null : _onSubmit,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: civicGreen, // Use logo color
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: _loading
                      ? const CircularProgressIndicator(color: Colors.white)
                      : const Text(
                    'Register',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                ),
              ),
              const SizedBox(height: 20),

              // --- Go to Login Button ---
              SizedBox(
                width: double.infinity,
                height: 50,
                child: OutlinedButton(
                  onPressed: () {
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(builder: (_) => const LoginScreen()),
                    );
                  },
                  style: OutlinedButton.styleFrom(
                    foregroundColor: civicBlue,
                    side: const BorderSide(color: civicBlue, width: 1.5),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: const Text(
                    'Already have an account? Login',
                    style: TextStyle(fontSize: 16),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}