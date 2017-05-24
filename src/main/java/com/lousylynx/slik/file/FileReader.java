package com.lousylynx.slik.file;

import lombok.Data;

import java.io.InputStream;

@Data
public abstract class FileReader {

    protected final String file;

    public abstract String read();

    public abstract InputStream getStream();
}
