package mail;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.*;
import java.util.*;

public class DailyEmailReport {

    // Email credentials and recipient details
    private static final String SMTP_HOST = "smtp.gmail.com"; // Your SMTP server
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "satheeshfintekpoint@gmail.com";
    private static final String RECEIVER_EMAIL = "nikeethakumar.29@gmail.com";
    private static final String PASSWORD = "prum dgmr xhit rcmy";
    private static final String SUBJECT = "Daily Test Report for Karate JUnit 5 Hybrid API Testing";
    private static final String BODY = "Dear Team,\n\nPlease find the attached daily test report for the Karate JUnit 5 hybrid framework.\n\nBest regards,\nSatheesh";
    private static final String REPORT_FILE_PATH = "target/extent-report.pdf";

    public static void main(String[] args) {
        sendEmailReport();
        System.out.println("✅ Email sent successfully.");
    }
    public static void sendEmailReport() {
        // Setup properties for the email server
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug",true);

        // Get session and authenticate
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, PASSWORD);
            }
        });

        try {
            // Create a MimeMessage to send the email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(RECEIVER_EMAIL));
            message.setSubject(SUBJECT);
            message.setText(BODY);

            // Create the file attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(REPORT_FILE_PATH);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(new File(REPORT_FILE_PATH).getName());

            // Create Multipart to hold the body and attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
