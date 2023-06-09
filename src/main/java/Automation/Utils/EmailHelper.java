package Automation.Utils;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * This Class will be used for sending mail
 *
 * @author mukesh.rajput
 */

public class EmailHelper
{

    private static Store store = null;

    Config testConfig;
    String emailId;
    List<HashMap<String, String>> completeMailList = null;
    int retry = 5;

    /**
     * Constructor
     *
     * @param testConfig - object of config
     * @param emailId    - email address
     */
    public EmailHelper(Config testConfig, String emailId)
    {
        this.testConfig = testConfig;
        this.emailId = emailId;
    }

    /**
     * This function is used to send the Automation report emails
     *
     * @param sendEmailTo        - Comma Separated List of Email Id to which we want to send Email
     * @param subject            - Subject/Heading of the Email
     * @param messageContent     - Content/Full message that we want to send
     * @param emailContentType   - Defines the type of Content we want to send in the Email
     * @param attachmentFilePath - Array of file paths of all the files we need to send as attachments
     */
    public static void sendEmail(String sendEmailTo, String subject, String messageContent, EmailContentType emailContentType, String[] attachmentFilePath)
    {
        if (sendEmailTo == null)
        {
            System.out.println("Email not sent, as email id is blank!");
        } else
        {
            boolean isEmailSent = false;
            String reportName = "Thanos - QA Automation";
            String username = CommonUtilities.decryptMessage(System.getProperty("ThanosGmailUsername").getBytes());
            String password = CommonUtilities.decryptMessage(System.getProperty("ThanosGmailPassword").getBytes());

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            Session session = Session.getInstance(properties, new javax.mail.Authenticator()
            {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication()
                {
                    return new javax.mail.PasswordAuthentication(username, password);
                }
            });

            try
            {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username, reportName));
                message.setReplyTo(InternetAddress.parse(sendEmailTo));
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(sendEmailTo));
                message.setSubject(subject);

                switch (emailContentType)
                {
                    case Html:
                        message.setContent(messageContent, "text/html; charset=UTF-8");
                        break;
                    case HtmlWithAttachment:
                        if (attachmentFilePath != null)
                        {
                            MimeMultipart multipart = new MimeMultipart("related");
                            MimeBodyPart messageBodyPart = new MimeBodyPart();
                            messageBodyPart.setContent(messageContent, "text/html; charset=UTF-8");
                            multipart.addBodyPart(messageBodyPart);
                            for (int i = 0; i < attachmentFilePath.length; i++)
                            {
                                int index = attachmentFilePath[i].lastIndexOf(File.separator);
                                String fileName = attachmentFilePath[i].substring(index + 1);
                                MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                                DataSource source = new FileDataSource(attachmentFilePath[i]);
                                messageBodyPart2.setDataHandler(new DataHandler(source));
                                messageBodyPart2.setFileName(fileName);
                                multipart.addBodyPart(messageBodyPart2);
                            }
                            message.setContent(multipart);
                        } else
                        {
                            System.out.println("Attachment File Name not specified or passed as null");
                        }
                        break;
                    default:
                        System.out.println("Email Content type not defined");
                }

                try
                {
                    Transport.send(message);
                    isEmailSent = true;
                } catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("********** This is the Message we were trying to send via Email **********\n");
                    System.out.println("Subject : " + message.getSubject());
                    try
                    {
                        System.out.println("Message : " + message.getContent().toString());
                    } catch (IOException e1)
                    {
                    }
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if (isEmailSent)
            {
                System.out.println(reportName + " sent to Email : " + sendEmailTo);
            } else
            {
                System.out.println("Email sending failed for : " + sendEmailTo);
            }
        }
    }

    /*
     * This method is to establish a connection with Gmail
     */
    private static Store initiateGmailConnection(Config testConfig)
    {
        if (store == null)
        {
            String username = CommonUtilities.decryptMessage(System.getProperty("ThanosGmailUsername").getBytes());
            String password = CommonUtilities.decryptMessage(System.getProperty("ThanosGmailPassword").getBytes());

            Properties properties = new Properties();
            properties.setProperty("mail.host", "imap.gmail.com");
            properties.setProperty("mail.port", "995");
            properties.setProperty("mail.transport.protocol", "imaps");
            Session session = Session.getInstance(properties, new javax.mail.Authenticator()
            {
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(username, password);
                }
            });

            testConfig.logComment("connecting to the Gmail account..");

            try
            {
                store = session.getStore("imaps");
                store.connect();
            } catch (NoSuchProviderException e)
            {
                testConfig.logComment("Store object not found for given imaps protocol.");
                testConfig.logExceptionAndFail(e);
            } catch (MessagingException e)
            {
                testConfig.logComment("Could not connect to the Gmail account.");
                testConfig.logExceptionAndFail(e);
            }
        }
        return store;
    }

    /**
     * This method is used to get content of the latest email from gmail for a particular mail subject along with an optional body content string and marks all emails for that subject as read.
     *
     * @param testConfig          - Pass object of config
     * @param subjectSearchString - string
     * @param maxWaitTime         - time in integer
     * @param bodySearchString    - string (optional)
     * @return mail body contents as string
     */
    public static String searchEmailFromSubjectAndReturnMailBodyForGmail(Config testConfig, String subjectSearchString, int maxWaitTime, String... bodySearchString)
    {

        Folder folder = null;
        Message[] messages = null;
        boolean mailFound = false;
        Message email = null;
        String emailContent = "";

        try
        {
            store = initiateGmailConnection(testConfig);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e)
        {
            testConfig.logComment("Could not connect to the Gmail account.");
            testConfig.logExceptionAndFail(e);
        }

        int waitInterval = maxWaitTime / 3;
        int tryCount = 0;
        while (tryCount++ < 3)
        {
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            SearchTerm searchTerm = new AndTerm(unseenFlagTerm, new SubjectTerm(subjectSearchString));
            if (bodySearchString != null && bodySearchString.length > 0)
            {
                searchTerm = new AndTerm(searchTerm, new BodyTerm(bodySearchString[0]));
                testConfig.logComment("Searching messages by subject :" + subjectSearchString + " and body content having : " + bodySearchString[0]);
            } else
            {
                testConfig.logComment("Searching messages by subject :" + subjectSearchString);
            }

            try
            {
                messages = folder.search(searchTerm, folder.getMessages());
            } catch (MessagingException e)
            {
                testConfig.logException("Messages cannot be searched due to complex search term or inaccessible folder or other reasons", e);
            }
            if (messages.length == 0)
            {
                testConfig.logComment("Waiting for email to be delivered");
                WaitHelper.waitForSeconds(testConfig, waitInterval);
            } else
            {
                break;
            }
        }

        for (Message mail : messages)
        {
            try
            {
                if (!mail.isSet(Flags.Flag.SEEN))
                {
                    email = mail;
                    mailFound = true;
                }
            } catch (MessagingException e)
            {
                testConfig.logException("Cannot check if given flag is set for message or not.", e);
            }
        }

        if (!mailFound)
        {
            testConfig.logComment("Email not found");
            return null;
        } else
        {
            try
            {
                if (email.isMimeType("multipart/*"))
                {
                    MimeMultipart mimeMultipart = (MimeMultipart) email.getContent();
                    emailContent = getHtmlFromMimeMultipart(mimeMultipart);
                } else
                {
                    emailContent = email.getContent().toString();
                }
            } catch (IOException | MessagingException e)
            {
                testConfig.logException("Cannot get content of the selected email message", e);
            }
        }

        return emailContent;
    }

    private static String getHtmlFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException
    {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++)
        {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/html"))
            {
                result = bodyPart.getContent().toString();
                break;
            }
        }
        return result;
    }

    /**
     * this method is to mark all the method as read.
     *
     * @param testConfig - Pass object of config
     */
    public static void markAllMessagesAsRead(Config testConfig)
    {
        try
        {
            store = initiateGmailConnection(testConfig);
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);
            Message[] allMessages = folderInbox.getMessages();
            testConfig.logComment("marking all messages in inbox as read");
            folderInbox.setFlags(allMessages, new Flags(Flags.Flag.SEEN), true);
            folderInbox.close(false);
        } catch (MessagingException e)
        {
            testConfig.logExceptionAndFail(e);
        }
    }

    /**
     * this method is to delete all emails from inbox, or delete emails on basis of subject along with an optional body content string
     *
     * @param testConfig          - Pass object of config
     * @param subjectSearchString - string
     * @param bodySearchString    - string (optional)
     */
    public static void deleteEmailFromInbox(Config testConfig, String subjectSearchString, Boolean makeInboxEmtpy, String... bodySearchString)
    {
        Folder folder = null;
        Message[] messages = null;

        try
        {
            store = initiateGmailConnection(testConfig);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e)
        {
            testConfig.logComment("Could not connect to the Gmail account.");
            testConfig.logExceptionAndFail(e);
        }

        try
        {
            if (makeInboxEmtpy)
            {
                messages = folder.getMessages();
                testConfig.logComment("Deleting all emails from inbox..");
                folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
            } else
            {
                SearchTerm searchTerm = new SubjectTerm(subjectSearchString);
                if (bodySearchString != null && bodySearchString.length > 0)
                {
                    searchTerm = new AndTerm(searchTerm, new BodyTerm(bodySearchString[0]));
                    testConfig.logComment("Deleting messages by subject :" + subjectSearchString + " and body content having : " + bodySearchString[0]);
                } else
                {
                    testConfig.logComment("Deleting messages by subject :" + subjectSearchString);
                }
                messages = folder.search(searchTerm, folder.getMessages());
                for (Message message : messages)
                {
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }
        } catch (MessagingException e)
        {
            testConfig.logExceptionAndFail(e);
        }
    }

    public static String searchEmailFromSubjectAndReturnMailBodyForGmailIncludingSeenMessages(Config testConfig, String subjectSearchString, int maxWaitTime, String... bodySearchString)
    {

        Folder folder = null;
        Message[] messages = null;
        boolean mailFound = false;
        Message email = null;
        String emailContent = "";

        try
        {
            store = initiateGmailConnection(testConfig);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e)
        {
            testConfig.logComment("Could not connect to the Gmail account.");
            testConfig.logExceptionAndFail(e);
        }

        int waitInterval = maxWaitTime / 3;
        int tryCount = 0;
        while (tryCount++ < 3)
        {
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
            SearchTerm searchTerm = new AndTerm(unseenFlagTerm, new SubjectTerm(subjectSearchString));
            if (bodySearchString != null && bodySearchString.length > 0)
            {
                searchTerm = new AndTerm(searchTerm, new BodyTerm(bodySearchString[0]));
                testConfig.logComment("Searching messages by subject :" + subjectSearchString + " and body content having : " + bodySearchString[0]);
            } else
            {
                testConfig.logComment("Searching messages by subject :" + subjectSearchString);
            }

            try
            {
                messages = folder.search(searchTerm, folder.getMessages());
            } catch (MessagingException e)
            {
                testConfig.logException("Messages cannot be searched due to complex search term or inaccessible folder or other reasons", e);
            }
            if (messages.length == 0)
            {
                testConfig.logComment("Waiting for email to be delivered");
                WaitHelper.waitForSeconds(testConfig, waitInterval);
            } else
            {
                break;
            }
        }

        for (Message mail : messages)
        {
            email = mail;
            mailFound = true;
        }

        if (!mailFound)
        {
            testConfig.logComment("Email not found");
            return null;
        } else
        {
            try
            {
                if (email.isMimeType("multipart/*"))
                {
                    MimeMultipart mimeMultipart = (MimeMultipart) email.getContent();
                    emailContent = getHtmlFromMimeMultipart(mimeMultipart);
                } else
                {
                    emailContent = email.getContent().toString();
                }
            } catch (IOException | MessagingException e)
            {
                testConfig.logException("Cannot get content of the selected email message", e);
            }
        }

        return emailContent;
    }

    /**
     * Get all the emails
     */
    public void getAllIncomingMails()
    {
        int count = 0;
        completeMailList = new ArrayList<>();
        while (completeMailList.isEmpty() && count <= retry)
        {
            try
            {
                org.jsoup.Connection.Response res = Jsoup.connect("http://www.yopmail.com/inbox.php?login=" + emailId.toLowerCase().replaceAll("@yopmail.com", "") + "&p=1&d=&ctrl=&scrl=&spam=true&yf=HZwD0ZGH5AwLlAGpjBGt0Aj&yp=YZGD0BQt3AGLjBGL4ZmNkBN&yj=RZGHjZmLlAwNkAmtmZGV4BN&v=2.8&r_c=&id=").timeout(50000).method(Method.GET).execute();
                Document doc = Jsoup.parse(res.body());
                Elements mails = doc.getElementsByClass("lm");
                if (Config.isDebugMode)
                {
                    testConfig.logComment("Response :" + doc);
                }
                for (Element mail : mails)
                {
                    HashMap<String, String> m = new HashMap<String, String>();
                    m.put("url", mail.attr("href"));
                    m.put("subject", mail.getElementsByAttributeValue("class", "lms").get(0).text());
                    completeMailList.add(m);
                }
                count++;
            } catch (Exception e)
            {
                testConfig.logExceptionAndFail(e);
            }
        }
    }

    public String getEmailBody(HashMap<String, String> mail)
    {
        try
        {
            org.jsoup.Connection.Response res = Jsoup.connect("http://www.yopmail.com/en/" + mail.get("url")).method(Method.GET).execute();
            if (Config.isDebugMode)
            {
                testConfig.logComment("Response :" + res.body());
            }
            return Jsoup.parse(res.body()).getElementById("mailmillieu").text();
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
            return null;
        }
    }

    public String getEmailForSubject(String subject)
    {
        String emailBody = null;
        int count = 0;
        while (emailBody == null && count <= retry)
        {
            WaitHelper.waitForSeconds(testConfig, 10);
            getAllIncomingMails();
            if (Config.isDebugMode)
            {
                testConfig.logComment("CompleteMailList : " + completeMailList);
            }
            for (HashMap<String, String> mail : completeMailList)
            {
                if (mail.get("subject").contains(subject))
                {
                    emailBody = getEmailBody(mail);
                    break;
                }
            }
            count++;
        }
        return emailBody;
    }

    public void verifyEmailContent(String actualEmailContent, HashMap<String, String> expectedData)
    {
        testConfig.logComment(emailId + " : " + actualEmailContent + " : " + expectedData);
        if (expectedData == null)
        {
            AssertHelper.assertNull(testConfig, "No mail is sent", actualEmailContent);
        } else
        {
            try
            {
                for (String key : expectedData.keySet())
                {
                    if (key == null || key.isEmpty() || key.equals("S.No"))
                    {
                        continue;
                    }
                    String expectedValue = testConfig.replaceArgumentsWithRunTimeProperties(expectedData.get(key));
                    if (expectedValue == null || expectedValue.equals("{skip}") || expectedValue.equals("null"))
                    {
                        continue;
                    }
                    AssertHelper.compareContains(testConfig, key + " in mail", expectedValue, actualEmailContent);
                }
            } catch (NullPointerException npe)
            {
                testConfig.logFail("Mail is not sent");
            }
        }
    }

    public enum EmailContentType
    {
        Html,
        HtmlWithAttachment
    }
}