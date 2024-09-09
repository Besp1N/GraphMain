package com.kacper.backend.mail;

import com.kacper.backend.utlils.Debug;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Mail controller class
 *
 * @Author Kacper Karabinowski
 */

@RestController
@RequestMapping("/public")
public class MailController
{
    private final MailService mailService;

    /**
     * @param mailService mail service injection
     */
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * @param to email address
     * @return response entity with status 200
     */
    @PostMapping("/send/{to}")
    public ResponseEntity<Void> send(@PathVariable String to) {
        mailService.sendMail(to);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
