package com.frankester.savefiles.controllers;

import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/files")
public class FileController {

    @Autowired
    FileService service;

    @GetMapping(path = "/")
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

    @PutMapping(path= "/{idFile}" )
    public ResponseEntity<Object> getSingleFile(@PathVariable("idFile") String idFile, @RequestBody RequestFile modifiedFile) throws FileNotFoundException {

        File savedFile = service.getOneFile(idFile);

        savedFile.setName(modifiedFile.getName());
        savedFile.setTypeFile(modifiedFile.getTypeFile());
        savedFile.setDescription(modifiedFile.getDescription());

        return ResponseEntity.ok(service.replaceFile(savedFile));
    }

    @PostMapping
    public ResponseEntity<Object> saveFile(@RequestBody RequestFile newFile){
        System.out.println(newFile);
        return ResponseEntity.ok(service.saveFile(newFile));
    }
}
