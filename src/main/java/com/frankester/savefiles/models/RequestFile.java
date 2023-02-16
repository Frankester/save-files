package com.frankester.savefiles.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestFile {

    private String name;
    private String typeFile;
    private String description;
}
