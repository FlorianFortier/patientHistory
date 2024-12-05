package com.abernathyclinic.patientHistory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "history")
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    private String id;
    private int patId;
    private String note;
    private String patient;
}
