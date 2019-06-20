package com.topica.btvn02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class FileTxtReader {

    public String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

}
