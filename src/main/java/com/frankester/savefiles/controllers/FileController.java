package com.frankester.savefiles.controllers;

import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/files")
public class FileController {

    @Autowired
    FileService service;

    @GetMapping
    public ResponseEntity<Object> getFiles(){
        return ResponseEntity.ok(service.getAllFiles());
    }

    @GetMapping(path = "/{idFile}")
    public ResponseEntity<Object> getSingleFile(@PathVariable("idFile") String idFile) throws FileNotFoundException {
        return ResponseEntity.ok(service.getOneFile(idFile));
    }

    @DeleteMapping(path = "/{idFile}")
    public ResponseEntity<Object> deleteFile(@PathVariable("idFile") String idFile) throws FileNotFoundException {
        return ResponseEntity.ok(service.deleteFile(idFile));
    }

    @PutMapping(path= "/{idFile}")
    public ResponseEntity<Object> getSingleFile(@PathVariable("idFile") String idFile, @RequestBody RequestFile modifiedFile) throws FileNotFoundException {

        File savedFile = service.getOneFile(idFile);

        savedFile.setName(modifiedFile.getName());
        savedFile.setTypeFile(modifiedFile.getTypeFile());
        savedFile.setDescription(modifiedFile.getDescription());

        return ResponseEntity.ok(service.replaceFile(savedFile));
    }

    @PostMapping
    public ResponseEntity<Object> saveFile(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        String typeFileExtension  = file.getOriginalFilename().split("\\.")[1];
        java.io.File realFile = new java.io.File("C:\\Users\\franc\\IdeaProjects\\savefiles\\uploads\\"+fileName);

        //save the file for uploading
        file.transferTo(realFile);

        RequestFile reqFileWithMetadata = new RequestFile();
        reqFileWithMetadata.setFile(realFile);
        reqFileWithMetadata.setName(fileName);
        reqFileWithMetadata.setTypeFile(typeFileExtension);

        File metadataFileSaved = service.saveFile(reqFileWithMetadata);

        // delete the file after uploading it
        realFile.delete();

      return ResponseEntity.ok(metadataFileSaved);
    }
}
