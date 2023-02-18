package com.frankester.savefiles.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository repo;

    @Autowired
    AmazonS3 s3;

    private static final Integer PageSize = 20;
    private static final String BucketName = "filebucketapp";

    @Override
    public Page<File> getAllFiles(){
        return repo.findAll(Pageable.ofSize(PageSize));
    }

    @Override
    public File getOneFile(String idFile) throws FileNotFoundException {
        File file = getFile(idFile);

        return file;
    }

    @Override
    public File saveFile(RequestFile newFile){
        java.io.File realFile = newFile.getFile();
        String fileName = newFile.getName();

        s3.putObject(BucketName,fileName,realFile);

        File metadataFile = newFile.toModelFile();

        return repo.save(metadataFile);
    }

    @Override
    public String deleteFile(String idFile) throws FileNotFoundException {

        File fileToDelete = getFile(idFile);
        String fileName = fileToDelete.getName();

        s3.deleteObject(BucketName, fileName);

        repo.deleteById(idFile);

        return "File successfully deleted";
    }

    @Override
    public File replaceFile(File newFile){
        newFile.addModifiedDate(LocalDateTime.now());

        //update the file
        return repo.save(newFile);
    }

    @Override
    public byte[] downloadOneFile(String idFile) throws FileNotFoundException, IOException {

        File file = getFile(idFile);
        String fileName = file.getName();

        S3Object objectFile = s3.getObject(BucketName, fileName);

        return convertObjectToByteArray(objectFile);
    }


    private File getFile(String idFile) throws FileNotFoundException {
        Optional<File> fileOp = repo.findById(idFile);

        if(fileOp.isEmpty()){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }

        return fileOp.get();
    }

    private byte[] convertObjectToByteArray(S3Object object) throws IOException {
        S3ObjectInputStream objectStream = object.getObjectContent();

        return  objectStream.readAllBytes();
    }
}
