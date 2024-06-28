package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.entity.*;
import com.example.exception.*;
import com.example.service.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    //
    // Account Endpoints
    //

    @PostMapping("/register")
    public Account registerAccount(@RequestBody Account account) {
        return accountService.persistAccount(account);
    }

    @PostMapping("/login")
    public Account loginAccount(@RequestBody Account account) {
        Account result = accountService.processLogin(account);
        if (result == null) {
            throw new InvalidLoginException();
        }
        return result;
    }

    //
    // Message Endpoints
    //

    @PostMapping("/messages")
    public Message postMessage(@RequestBody Message message) {
        Message result = messageService.persistMessage(message, this.accountService);
        if (result == null) {
            throw new ClientError();
        }
        return result;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessageByID(@PathVariable(name="messageId") int messageID) {
        return messageService.getMessageByID(messageID);
    }

    @DeleteMapping("/messages/{messageId}")
    public Integer deleteMessage(@PathVariable(name="messageId") int messageID) {
        return messageService.deleteMessage(messageID);
    }

    @PatchMapping("/messages/{messageId}")
    public Integer updateMessage(@PathVariable(name="messageId") int messageID, @RequestBody Message message) {
        Integer result = messageService.updateMessageText(messageID, message.getMessageText());
        if (result == null) {
            throw new ClientError();
        }
        return result;
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccount(@PathVariable(name="accountId") int accountID) {
        return messageService.getMessagesByUser(accountID);
    }

    //
    // Account Errors
    //

    @ExceptionHandler(DupilcateAccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleDuplicateAccount(DupilcateAccountException ex) {
        return;
    }

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleInvalidLogin(InvalidLoginException ex) {
        return;
    }

    //
    // Message Errors
    //

    @ExceptionHandler(ClientError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleGenericClientError(RuntimeException ex) {
        return;
    }

}
