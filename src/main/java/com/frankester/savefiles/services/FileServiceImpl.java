package com.frankester.savefiles.services;

import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository repo;

    private static final Integer PageSize = 20;

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

        return fileOp.get();
    }

    @Override
    public File saveFile(RequestFile newFile){

        File file = new File(null,newFile.getName(), newFile.getTypeFile(), newFile.getDescription(),null, null);
        return repo.save(file);
    }

    @Override
    public String deleteFile(String idFile) throws FileNotFoundException {

        if(repo.findAll().stream().anyMatch(f -> f.getId().equals(idFile))){
            throw new FileNotFoundException("File with id: "+ idFile+ " not found");
        }

        repo.deleteById(idFile);

        return "File deleted succesfull";
    }

    @Override
    public File replaceFile(File newFile){
        //update the file
        return repo.save(newFile);
    }
}
