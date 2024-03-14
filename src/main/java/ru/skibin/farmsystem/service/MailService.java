package ru.skibin.farmsystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skibin.farmsystem.api.data.other.Mail;
import ru.skibin.farmsystem.util.HtmlTableCreator;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MailService {
    private static final Logger LOGGER = Logger.getLogger(MailService.class.getName());
    private final JavaMailSender javaMailSender;
    private final StatisticService statisticService;
    private final HtmlTableCreator htmlTableCreator;
    @Value("${mail.sender}")
    private String emailSender;
    @Value("${mail.charset}")
    private String charset;
    @Value("${mail.target}")
    private String targetMail;
    private void sendEmail(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, charset);
        mimeMessageHelper.setTo(mail.getTargetEmail());
        mimeMessageHelper.setFrom(emailSender);
        mimeMessageHelper.setSubject("Farm: Day work statistic");
        mimeMessageHelper.setText(mail.getMessage(), true);
        javaMailSender.send(mimeMessage);
    }

    @Scheduled(cron = "${mail.cron}")
    private void computePrice() throws MessagingException {
        String table = htmlTableCreator.WorkersWithResultsToTable(
                statisticService.getWorkersWithResultsByDay(LocalDate.now())
        );
        Mail sendMail = new Mail(
                targetMail,
                table
        );
        sendEmail(sendMail);
        LOGGER.info("Send statistic to target mail.");
    }


}
