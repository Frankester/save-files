package com.frankester.savefiles.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        /*List<S3ObjectSummary> objects = s3.listObjectsV2(BucketName).getObjectSummaries();
        objects.forEach(obj -> System.out.println(obj.getKey()));
*/
        return repo.findAll(Pageable.ofSize(PageSize));
    }

    @Override
    public File getOneFile(String idFile) throws FileNotFoundException {

        Optional<File> fileOp = repo.findById(idFile);

        if(fileOp.isEmpty()){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }

        return fileOp.get();
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

        boolean exist = repo.findAll().stream().anyMatch(f -> f.getId().equals(idFile));
        if(!exist){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }

        repo.deleteById(idFile);

        return "File deleted succesfull";
    }

    @Override
    public File replaceFile(File newFile){
        newFile.addModifiedDate(LocalDateTime.now());

        //update the file
        return repo.save(newFile);
    }
}
