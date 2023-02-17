package com.frankester.savefiles.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestFile {

    private String name;
    private String typeFile;
    private String description;
    private java.io.File file;


    public File toModelFile(){
        File file = new File();

        file.setName(this.name);
        file.setTypeFile(this.typeFile);
        file.setDescription(this.description);

        return file;
    }
}
