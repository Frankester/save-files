package com.frankester.savefiles.controllers;

import com.frankester.savefiles.exceptions.FileNotFoundException;
import com.frankester.savefiles.models.File;
import com.frankester.savefiles.models.RequestFile;
import com.frankester.savefiles.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Object> getSingleFile(@PathVariable("idFile") String idFile) throws FileNotFoundException, IOException {

        byte[] fileToDownload = service.downloadOneFile(idFile);

        Resource fileResource = new ByteArrayResource(fileToDownload);

        File metadataFile = service.getOneFile(idFile);
        String fileName = metadataFile.getName();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                .body(fileResource);
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

        service.saveFile(file);

      return ResponseEntity.ok("El archivo" + file.getOriginalFilename() + " se guardado exitosamente.");
    }
}
