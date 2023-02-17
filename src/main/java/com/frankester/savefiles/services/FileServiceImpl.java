package com.frankester.savefiles.services;

import com.amazonaws.services.s3.AmazonS3;
import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        Optional<File> fileOp = repo.findById(idFile);

        if(fileOp.isEmpty()){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }

        File file = fileOp.get();
        
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

        Optional<File> fileToDeleteOp = repo.findById(idFile);

        if(fileToDeleteOp.isEmpty()){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }

        File fileToDelete = fileToDeleteOp.get();
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
}
