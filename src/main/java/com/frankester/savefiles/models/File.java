package com.frankester.savefiles.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document
public class File {

    @Id
    private String id;

    private String name;
    private String typeFile;
    private String description;
    private LocalDateTime created_at;
    private List<LocalDateTime> modified_at;

    public File(){
        this.modified_at= new ArrayList<>();
        this.created_at = LocalDateTime.now();
    }
}
