package gex.example.service

import gex.example.dto.v1.User

interface UserService {
  User findByUsername(String username)
  User createUser(User user)
  User updateUser(String username, User user)
  void deleteUser(String username)
}

