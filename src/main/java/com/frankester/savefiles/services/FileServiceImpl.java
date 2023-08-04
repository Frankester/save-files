package com.frankester.savefiles.services;

import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository repo;

    @Autowired
    S3Client s3;

    private static final Integer PageSize = 20;

    @Value("${aws.bucketName}")
    private String BucketName ;

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
    public void saveFile(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.BucketName)
                .key(fileName)
                .build();

        this.s3.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        String fileExtension = fileName.split("\\.")[1];

        File metadataFile = new File();
        metadataFile.setName(fileName);
        metadataFile.setTypeFile(fileExtension);

        this.repo.save(metadataFile);
    }

    @Override
    public String deleteFile(String idFile) throws FileNotFoundException {

        File fileToDelete = getFile(idFile);
        String fileName = fileToDelete.getName();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(this.BucketName)
                .key(fileName)
                .build();

        this.s3.deleteObject(deleteObjectRequest);

        this.repo.deleteById(idFile);

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

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(this.BucketName)
                .key(fileName)
                .build();

        ResponseInputStream<GetObjectResponse> getObjectResponse = s3.getObject(getObjectRequest);

        return getObjectResponse.readAllBytes();
    }


    private File getFile(String idFile) throws FileNotFoundException {
        Optional<File> fileOp = repo.findById(idFile);

        if(fileOp.isEmpty()){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }
        String fileName = fileOp.get().getName();

        if(!existsFileWithName(fileName)){
            throw new FileNotFoundException("File with name: "+fileName+" not found");
        }

        return fileOp.get();
    }

    private boolean existsFileWithName(String filename){
        try{
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .key(filename)
                    .bucket(this.BucketName)
                    .build();

            this.s3.headObject(headObjectRequest);

            return true;
        } catch(S3Exception exception){
            if(exception.statusCode() == 404){
                return false;
            }
        }
        return false;
    }
}
