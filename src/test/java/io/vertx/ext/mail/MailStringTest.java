package io.vertx.ext.mail;

import static org.hamcrest.core.StringContains.containsString;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.subethamail.wiser.WiserMessage;

/**
 * Test sending message with pregenerated String
 *
 * this test uses Wiser since it is easier to assert the parsed message
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 *
 */
public class MailStringTest extends SMTPTestWiser {

  @Test
  public void mailTest() throws MessagingException, IOException {
    MailService mailService = mailServiceTLSTrustAll();

    MailMessage email = exampleMessage();

    // note that the to and from fields from the string are not
    // evaluated at all

    String messageString = "Message-ID: <12345@example.com>\n" + 
        "Date: Mon, 09 Mar 2015 22:10:48 +0100\n" + 
        "From: User Name <person@example.com>\n" + 
        "MIME-Version: 1.0\n" + 
        "To: User Name <person@example.net>\n" + 
        "Subject: pregenerated message\n" + 
        "Content-Type: text/plain; charset=US-ASCII\n" + 
        "Content-Transfer-Encoding: 7bit\n" + 
        "\n" + 
        "this is an example mail\n" + 
        "\n";

    testSuccess(mailService, email, messageString);

    final WiserMessage message = wiser.getMessages().get(0);
    assertEquals("from@example.com", message.getEnvelopeSender());
    final MimeMessage mimeMessage = message.getMimeMessage();
    assertThat(mimeMessage.getContentType(), containsString("text/plain"));
    assertEquals("pregenerated message", mimeMessage.getSubject());
    assertEquals("this is an example mail\n\n\n", TestUtils.conv2nl(inputStreamToString(mimeMessage.getInputStream())));
  }

}
