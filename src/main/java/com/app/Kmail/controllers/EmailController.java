package com.app.Kmail.controllers;

import com.app.Kmail.model.binding.EmailSendBindingModel;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.service.EmailService;
import com.app.Kmail.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/emails")
public class EmailController {

    private final UserService userService;
    private final EmailService emailService;
    private Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    public EmailController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public String sendEmailGet() {
        return "send-email";
    }

    @PostMapping("/send")
    public String postEmail(@Valid EmailSendBindingModel emailSendBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Principal principal) throws IOException {

        //check if fields are not null
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailSendBindingModel", emailSendBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailSendBindingModel", bindingResult);
            return "redirect:/emails/send";
        }

        //check if the user that this message has been send from is the same as the logged in user
        if (!emailSendBindingModel.getFrom().equals(principal.getName() + "@kmail.com")) {
            redirectAttributes.addFlashAttribute("errorFrom", true);
            redirectAttributes.addFlashAttribute("emailSendBindingModel", emailSendBindingModel);
            return "redirect:/emails/send";
        }

        //check if the user exists;
        boolean isAddressInvalid = !emailSendBindingModel.getTo().endsWith("@kmail.com");
        if (isAddressInvalid) {
            redirectAttributes.addFlashAttribute("errorTo", true);
            redirectAttributes.addFlashAttribute("emailSendBindingModel", emailSendBindingModel);
            return "redirect:/emails/send";
        } else {
            boolean isUsernameTaken = userService.isUsernameTaken(userService.removeAddress(emailSendBindingModel.getTo()));
            if (!isUsernameTaken) {
                redirectAttributes.addFlashAttribute("errorTo", true);
                redirectAttributes.addFlashAttribute("emailSendBindingModel", emailSendBindingModel);
                return "redirect:/emails/send";

            }
        }
        emailService.save(toServiceModel(emailSendBindingModel));
        return "redirect:/";
    }

    private EmailServiceModel toServiceModel(EmailSendBindingModel emailSendBindingModel) throws IOException {
        EmailServiceModel emailModel = new EmailServiceModel();
        emailModel.setTo(userService.removeAddress(emailSendBindingModel.getTo()))
                .setFrom(userService.removeAddress(emailSendBindingModel.getFrom()))
                .setAttachment(multipartToByteArray(emailSendBindingModel
                        .getAttachment()))
                .setContent(emailSendBindingModel.getContent())
                .setTitle(emailSendBindingModel.getTitle())
                .setCreated(LocalDateTime.now())
                .setRead(false);
        return emailModel;
    }

    //this method will convert out multipart file to array of bytes
    private byte[] multipartToByteArray(MultipartFile attachment) {
        try {
            if (attachment.isEmpty()) return null;
            return attachment.getBytes();
        } catch (IOException e) {
            return null;
        }
    }

    @ModelAttribute("emailSendBindingModel")
    public EmailSendBindingModel emailSendBindingModel(Principal principal) {
        return new EmailSendBindingModel().setFrom(principal.getName() + "@kmail.com");
    }
}
