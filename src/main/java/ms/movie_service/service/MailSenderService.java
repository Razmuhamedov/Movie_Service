package ms.movie_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    @Autowired
    private MailSender mailSender;
    public void send(String toAccount, String title, String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAccount);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(content);

        mailSender.send(simpleMailMessage);
    }
}
