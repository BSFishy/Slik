package com.lousylynx.slik.file;

import com.lousylynx.slik.Slik;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;

public class InternalFileReader extends FileReader {
    public InternalFileReader(String file) {
        super(file.startsWith("/") ? file.substring(1) : file);
    }

    @Override
    public String read() {
        try {
            return IOUtils.toString(getStream(), "UTF-8");
        } catch (IOException e) {
            Slik.getLOG().error("There was an error reading the file: " + file);
            Slik.getLOG().log(Level.ERROR, e.getMessage(), e);

            return "";
        }
    }

    @Override
    public InputStream getStream() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.getResourceAsStream(file);
    }
}
