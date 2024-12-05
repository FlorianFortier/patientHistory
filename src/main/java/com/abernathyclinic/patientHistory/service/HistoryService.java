package com.abernathyclinic.patientHistory.service;

import com.abernathyclinic.patientHistory.model.History;
import com.abernathyclinic.patientHistory.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing patient medical history.
 * <p>
 * This service provides methods to retrieve, save, update, and delete patient history records.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    /**
     * Retrieves all medical history records for a specific patient by their ID.
     *
     * @param patientId the ID of the patient whose history is to be retrieved.
     * @return a list of {@link History} objects associated with the patient.
     */
    public List<History> getHistoryByPatientId(int patientId) {
        return historyRepository.findByPatId(patientId);
    }

    /**
     * Adds a new note to the medical history of a specific patient.
     *
     * @param patientId the ID of the patient to whom the note will be added.
     * @param note      the {@link History} note to add to the patient's history.
     */
    public void addNoteToPatientHistory(int patientId, History note) {
        note.setPatId(patientId);
        historyRepository.save(note);
    }

    /**
     * Saves or updates a medical history record.
     *
     * @param history the {@link History} object to be saved or updated.
     * @return the saved {@link History} object.
     */
    public History saveHistory(History history) {
        return historyRepository.save(history);
    }

    /**
     * Retrieves a specific medical history record by its ID.
     *
     * @param id the ID of the history record to retrieve.
     * @return an {@link Optional} containing the {@link History} record if found, or empty if not.
     */
    public Optional<History> getHistoryById(String id) {
        return historyRepository.findById(id);
    }

    /**
     * Deletes a specific medical history record by its ID.
     *
     * @param id the ID of the history record to delete.
     * @throws IllegalArgumentException if the history record with the specified ID does not exist.
     */
    public void deleteHistoryById(String id) {
        if (!historyRepository.existsById(id)) {
            throw new IllegalArgumentException("L'historique avec l'ID " + id + " n'existe pas.");
        }
        historyRepository.deleteById(id);
    }
}
