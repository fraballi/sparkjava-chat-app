package com.asapp.backend.challenge.service;

import javax.validation.Valid;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.repository.UserRepository;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.dto.UserResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@RequiredArgsConstructor
public final class UserService {

   private final UserRepository userRepository;

   public UserResource findByUsername(final String username) throws ApiException {
      return userRepository.findByUsername(username);
   }

   public UserResponse create(@Valid final UserResource userResource) throws ApiException {
      return userRepository.create(userResource);
   }
}
