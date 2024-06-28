package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ClientError;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message persistMessage(Message message, AccountService accountService) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return null;
        }
        if (accountService.getAccountByID(message.getPostedBy()) == null) {
            return null;
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageByID(int messageID) {
        return messageRepository.findById(messageID).orElse(null);
    }
    
    public Integer deleteMessage(int messageID) {
        Optional<Message> message = messageRepository.findById(messageID);
        if (message.isPresent()) {
            messageRepository.delete(message.get());
            return 1;
        }
        return null;
    }

    public Integer updateMessageText(int messageID, String messageText) {
        if (messageText.isBlank() || messageText.length() > 255) {
            return null;
        }
        Message message = messageRepository.findById(messageID).orElse(null);
        if (message == null) {
            return null;
        }
        message.setMessageText(messageText);
        messageRepository.save(message);
        return 1;
    }

    public List<Message> getMessagesByUser(int postedBy) {
        return messageRepository.findMessagesByPostedBy(postedBy);
    }

}
