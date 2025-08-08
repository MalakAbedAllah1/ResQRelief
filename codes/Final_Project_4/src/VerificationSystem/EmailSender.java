package VerificationSystem;

//Sub Classes
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {



    public static void sendQueuePositionEmail(String recipientEmail, String customerName, int position, int totalCustomers) {
        final String yourEmail = "efdsfp19@gmail.com";
        final String yourPassword = "umstwccwosjgohod";  //generated password by working to connect java to our company email(by 2 step verification)

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); // Authentication- sign in using e-mail and password (مصادقة)
        props.put("mail.smtp.starttls.enable", "true");  //Encryption using TSL to save the e-mail while sending (تشفير)
        props.put("mail.smtp.host", "smtp.gmail.com"); //choosing the SMTP server (خادم الايميلات) to send the e-mail (here choosed gmail)
        props.put("mail.smtp.port", "587"); // port (المنفذ) (chose 587 to use with TLS)

        Session session = Session.getInstance(props,   //this function is for starting a new session (מוֹשָׁב) by giving it the object "props" that contains the needed information to send an email
                new javax.mail.Authenticator() {    //we are creating an anonymous subclass of Authenticator that overrides the getPasswordAuthentication() method to provide the email and password
                    protected PasswordAuthentication getPasswordAuthentication() {  /*
                                                                                        public:whole access
                                                                                        private:just for the class itself
                                                                                        protected:the class itself+any classes in the package+any subclasses-imported-
                                                                                         */
                        return new PasswordAuthentication(yourEmail, yourPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session); // It is allowed to send messages containing: Texts, Attachments, HTML format
            message.setFrom(new InternetAddress(yourEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));  //to send the email for the recipient (here we chosed TO-direct recipient) which send directly to the e-mail

            message.setSubject("Your Queue Position: #" + position + " - Order Confirmation");

            String emailContent = "Dear " + customerName + ",\n\n" +
                    "Thank you for submitting your order request.\n\n" +
                    "Your application has been received and processed successfully.\n" +
                    "Based on our priority system, your current position in the queue is:\n\n" +
                    "🏷️ Queue Position: " + position + " out of " + totalCustomers + " customers\n\n" +
                    "We will notify you when it's your turn.\n\n" +
                    "Best regards,\n" +
                    "Customer Service Team";

            message.setText(emailContent);

            Transport.send(message);
            System.out.println("📧 Queue position email sent to " + recipientEmail + " (Position: " + position + "/" + totalCustomers + ")");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}