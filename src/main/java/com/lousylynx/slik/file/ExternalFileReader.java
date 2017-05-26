package com.lousylynx.slik.file;

import com.lousylynx.slik.Slik;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ExternalFileReader extends FileReader {
    public ExternalFileReader(String file) {
        super(file);
    }

    @Override
    public String read() {
        return null;
    }

    @Nullable
    @Override
    public InputStream getStream() {
        File f = new File(file);

        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            Slik.getLOG().error("There was an error loading the following file: " + f.getName());
            Slik.getLOG().error(e.getMessage(), e);
            return null;
        }
    }
}
