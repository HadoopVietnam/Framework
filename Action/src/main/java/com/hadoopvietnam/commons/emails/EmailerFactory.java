/*
 * Copyright 2012 Hadoop Vietnam <admin@hadoopvietnam.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hadoopvietnam.commons.emails;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
/**
 * Email helper for sending Emails.
 */
public class EmailerFactory {

    /**
     * Protocol message is using (e.g. "smtp").
     */
    private final VelocityEngine velocityEngine;
    /**
     * Address of sender if not otherwise specified.
     */
    private final String defaultFromAddress;
    /**
     * Java mail sender.
     */
    private final JavaMailSender mailSender;

//    private Ve
    /**
     * Constructor.
     *
     * @param inVelocityEngine Velocity Engine message is using (e.g. "smtp").
     * @param inMailSender Java Mail Sender.
     * @param inDefaultFromAddress Address of sender if not otherwise specified.
     */
    public EmailerFactory(final VelocityEngine inVelocityEngine, final JavaMailSender inMailSender, final String inDefaultFromAddress) {
        velocityEngine = inVelocityEngine;
        mailSender = inMailSender;
        defaultFromAddress = inDefaultFromAddress;
    }

    /**
     * Velocity Engine template.
     *
     * @return
     */
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * @param message message to be sent.
     * @throws MessagingException Thrown if there are problems sending the
     * message.
     */
    public void sendMail(final Message message) throws MessagingException {
        Transport.send(message);
    }

    /**
     * @param message message to be sent.
     * @throws MessagingException Thrown if there are problems sending the
     * message.
     */
    public void sendMail(final MimeMessage message) throws MessagingException {
        mailSender.send(message);
    }

    /**
     * Creates a "blank" email message, ready for the application to set the
     * content (subject, body, etc.).
     *
     * @return An email message.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public MimeMessage createMessage() throws MessagingException, Exception {
        MimeMessage msg = mailSender.createMimeMessage();
        msg.setSentDate(new Date());
        msg.setFrom(new InternetAddress(defaultFromAddress, "Mạng Tuyển Dụng"));
        msg.setContent(new MimeMultipart("alternative"));
        setReplyTo(msg, defaultFromAddress);
//        set
        return msg;
    }

    /**
     * Sets the primary ('to') recipients.
     *
     * @param message Email message being built.
     * @param emailToString Comma delimited list of email addresses Email is
     * being sent to.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setTo(final MimeMessage message, final String emailToString) throws MessagingException {
        message.setRecipients(RecipientType.TO, emailToString);
    }

    /**
     * Sets the CC recipients.
     *
     * @param message Email message being built.
     * @param emailToString Comma delimited list of email addresses Email is
     * being sent to.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setCc(final MimeMessage message, final String emailToString) throws MessagingException {
        message.setRecipients(RecipientType.CC, emailToString);
    }

    /**
     * Sets the BCC recipients.
     *
     * @param message Email message being built.
     * @param emailToString Comma delimited list of email addresses Email is
     * being sent to.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setBcc(final MimeMessage message, final String emailToString) throws MessagingException {
        message.setRecipients(RecipientType.BCC, emailToString);
    }

    /**
     * @param message Email message being built.
     * @param emailFromString Email Address of person sending the email.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setFrom(final MimeMessage message, final String emailFromString) throws MessagingException {
        InternetAddress fromAddress = new InternetAddress(emailFromString);
        message.setFrom(fromAddress);
    }

    /**
     * Sets the reply-to address.
     *
     * @param message Email message being built.
     * @param addressString Email address to use.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setReplyTo(final MimeMessage message, final String addressString) throws MessagingException {
        message.setReplyTo(new Address[]{new InternetAddress(addressString)});
    }

    /**
     * @param message Email message being built.
     * @param subject Subject of the Email.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setSubject(final MimeMessage message, final String subject) throws MessagingException {
        message.setSubject(subject);
    }

    /**
     * @param message Email message being built.
     * @param textBody Plain Text Email Body.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setTextBody(final MimeMessage message, final String textBody) throws MessagingException {
        BodyPart textBp = new MimeBodyPart();
        textBp.setText(textBody);
        getMultipartAlternative(message).addBodyPart(textBp);
    }

    /**
     * @param message Email message being built.
     * @param htmlBody Rich HTML body of the Email.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public void setHtmlBody(final MimeMessage message, final String htmlBody) throws MessagingException {
        BodyPart htmlBp = new MimeBodyPart();
        htmlBp.setContent(htmlBody, "text/html");
        htmlBp.setHeader("MIME-VERSION", "1.0");
        htmlBp.setHeader("Content-Type", "text/html; charset=utf-8");
        getMultipartAlternative(message).addBodyPart(htmlBp);
    }

    /**
     * Adds an attachment part to the message. Caller must put content into the
     * returned part.
     *
     * @param message Email message being built.
     * @return Newly-created part.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public Part addAttachmentPart(final Message message) throws MessagingException {
        Multipart container = getMultipart(message);
        String contentSubtype = new ContentType(container.getContentType()).getSubType();
        if ("alternative".equals(contentSubtype)) {
            Multipart alternative = container;

            // create new multipart/mixed as top-level part
            container = new MimeMultipart("mixed");
            message.setContent(container);

            // add existing multipart/alternative as first item in it
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(alternative);
            container.addBodyPart(bodyPart);
        }

        // create attachment part
        BodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.setDisposition(Part.ATTACHMENT);
        container.addBodyPart(attachmentBodyPart);

        return attachmentBodyPart;
    }

    /**
     * Adds an existing message as an attachment to the message.
     *
     * @param message Email message being built.
     * @param attachedMessage Another email message to be added as an
     * attachment.
     * @return Newly-created part.
     * @throws MessagingException Thrown if there are problems creating the
     * message.
     */
    public Part addAttachmentMessage(final Message message, final Message attachedMessage) throws MessagingException {
        Part part = addAttachmentPart(message);
        DataHandler dh = new DataHandler(attachedMessage, "message/rfc822");
        part.setDataHandler(dh);

        return part;
    }

    /**
     * Retrieves the top-level multipart object from the message.
     *
     * @param message Email message being built.
     * @return multipart object.
     * @throws MessagingException If there is a problem retrieving the content.
     */
    protected Multipart getMultipart(final Message message) throws MessagingException {
        try {
            return (Multipart) message.getContent();
        } catch (IOException ex) {
            throw new MessagingException("Failed to retrieve multipart from message being built.", ex);
        }
    }

    /**
     * Retrieves the multipart/alternative object from the message. This method
     * makes some significant assumptions about the layout of the message, but
     * ones that would be true if the message has been built solely using this
     * class.
     *
     * @param message Email message being built.
     * @return multipart object.
     * @throws MessagingException If there is a problem retrieving the content.
     */
    protected Multipart getMultipartAlternative(final Message message) throws MessagingException {
        try {
            Multipart container = (Multipart) message.getContent();
            String contentSubtype = new ContentType(container.getContentType()).getSubType();
            if ("mixed".equals(contentSubtype)) {
                container = (Multipart) container.getBodyPart(0).getContent();
            }
            return container;
        } catch (IOException ex) {
            throw new MessagingException("Failed to retrieve content from message being built.", ex);
        }
    }
}
