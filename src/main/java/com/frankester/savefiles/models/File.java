package com.frankester.savefiles.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", typeFile='" + typeFile + '\'' +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", modified_at=" + modified_at +
                '}';
    }
}
