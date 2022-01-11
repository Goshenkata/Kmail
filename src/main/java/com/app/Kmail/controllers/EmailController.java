package com.app.Kmail.controllers;

import com.app.Kmail.model.binding.EmailSendBindingModel;
import com.app.Kmail.model.service.EmailServiceModel;
import com.app.Kmail.model.view.EmailViewModel;
import com.app.Kmail.model.view.InboxViewModel;
import com.app.Kmail.service.EmailService;
import com.app.Kmail.service.UserService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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
                .setAttachmentName(
                        emailSendBindingModel.getAttachment().getOriginalFilename())
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

    @GetMapping("/inbox")
    public String inbox(Principal principal, Model model) {
        List<InboxViewModel> emails = emailService
                .getAllEmailsForUser(principal.getName());
        if (emails.isEmpty()) {
            model.addAttribute("isEmpty", true);
        } else {
            model.addAttribute("emails", emails);
            model.addAttribute("isSent", false);
        }
        return "inbox";
    }

    @GetMapping("/sent")
    public String sent(Principal principal, Model model) {
        List<InboxViewModel> emails = emailService
                .getAllEmailsFromUser(principal.getName());
        if (emails.isEmpty()) {
            model.addAttribute("isEmpty", true);
        } else {
            model.addAttribute("emails", emails);
            model.addAttribute("isSent", true);
        }
        return "inbox";
    }

    @PreAuthorize("@emailServiceImpl.canViewEmail(#id, #principal.name)")
    @GetMapping("/{id}") public String viewEmail(@PathVariable("id") Long id,
                            Principal principal,
                            Model model) {
        emailService.emailSeen(id, principal.getName());
        EmailViewModel emailViewModel = emailService.getEmailViewModel(id, principal);
        model.addAttribute("email", emailViewModel);
        return "email";
    }

    @PreAuthorize("@emailServiceImpl.canViewEmail(#id, #principal.name)")
    @GetMapping("/{id}/download")
    @ResponseBody
    public FileSystemResource downloadAttachment(@PathVariable Long id,
                                                 Principal principal) throws IOException {
        return emailService.downloadAttachment(id);
    }

}
