package com.abernathyclinic.patientHistory.controller;

import com.abernathyclinic.patientHistory.model.History;
import com.abernathyclinic.patientHistory.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing patient history.
 * <p>
 * Provides endpoints to retrieve and add medical history records for patients.
 * </p>
 */
@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    /**
     * Retrieves the complete medical history of a patient by their ID.
     *
     * @param patientId the ID of the patient whose history is to be retrieved.
     * @return a {@link ResponseEntity} containing a list of {@link History} records if found,
     *         or a 204 No Content response if no history exists for the patient.
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<List<History>> getPatientHistory(@PathVariable int patientId) {
        List<History> histories = historyService.getHistoryByPatientId(patientId);
        if (histories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(histories);
    }

    /**
     * Adds a new note to a patient's medical history.
     *
     * @param patientId the ID of the patient to whom the note will be added.
     * @param note      the {@link History} note to add to the patient's history.
     * @return a {@link ResponseEntity} with a 200 OK status if the note is added successfully,
     *         or a 500 Internal Server Error status if an error occurs during the operation.
     */
    @PostMapping("/{patientId}/add")
    public ResponseEntity<Void> addNoteToPatientHistory(
            @PathVariable int patientId,
            @RequestBody History note) {
        try {
            historyService.addNoteToPatientHistory(patientId, note);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
