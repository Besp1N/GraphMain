package com.kacper.backend.mail;

import com.kacper.backend.utlils.Debug;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class MailController
{
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send/{to}")
    public ResponseEntity<Void> send(@PathVariable String to) {
        mailService.sendMail(to);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
