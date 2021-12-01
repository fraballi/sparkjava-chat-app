package com.asapp.backend.challenge.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.repository.MessageRepository;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.utils.dto.MessageFullResponse;
import com.asapp.backend.challenge.utils.dto.MessageResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@RequiredArgsConstructor
public final class MessageService {

   private final MessageRepository messageRepository;

   public MessageResponse create(final MessageResource messageResource) throws ApiException {
      return messageRepository.create(messageResource);
   }

   public Map<String, Collection<MessageFullResponse>> getAll(final Integer recipient, final Integer start, final Integer limit) throws ApiException {

      final var messages = messageRepository.paginate(recipient, start, limit);
      final var response = new HashMap<String, Collection<MessageFullResponse>>();
      response.put("messages", messages);

      return response;
   }
}
