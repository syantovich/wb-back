package org.syantovich.wbpublic.services;

public interface MessageSender {
    boolean send(String to, String subject, String text);
}
