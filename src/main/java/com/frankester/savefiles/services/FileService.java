package com.frankester.savefiles.services;

import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface FileService {
    public Page<File> getAllFiles();

    public File getOneFile(String idFile) throws FileNotFoundException;

    public File saveFile(RequestFile newFile);

    public String deleteFile(String idFile) throws FileNotFoundException;

    public File replaceFile(File newFile);

    public byte[] downloadOneFile(String idFile) throws FileNotFoundException, IOException;
}
