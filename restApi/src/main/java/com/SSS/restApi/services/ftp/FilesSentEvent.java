package com.sss.restapi.services.ftp;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FilesSentEvent extends ApplicationEvent {
    private final String filename;
    public FilesSentEvent(Object source, String filename) {
        super(source);
        this.filename = filename;
    }

}
