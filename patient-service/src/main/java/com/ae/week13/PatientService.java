package com.ae.week13;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class PatientService {

	private final PatientRepository patientRepository;

	public PatientService(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public List<Patient> findAll() {
		return patientRepository.findAll();
	}

	public Patient findById(Integer id) {
		return patientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Patient not found: " + id));
	}

	public List<Patient> search(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return findAll();
		}

		String trimmedKeyword = keyword.trim();
		return patientRepository
				.findByPatientMobileContainingIgnoreCaseOrPatientEmailContainingIgnoreCaseOrPatientNameContainingIgnoreCaseOrPatientAddressContainingIgnoreCaseOrPatientUsernameContainingIgnoreCase(
						trimmedKeyword, trimmedKeyword, trimmedKeyword, trimmedKeyword, trimmedKeyword);
	}

	public Patient create(Patient patient) {
		patient.setPatientId(null);
		return patientRepository.save(patient);
	}

	public Patient update(Integer id, Patient patient) {
		Patient existingPatient = findById(id);
		existingPatient.setPatientMobile(patient.getPatientMobile());
		existingPatient.setPatientEmail(patient.getPatientEmail());
		existingPatient.setPatientName(patient.getPatientName());
		existingPatient.setPatientPassword(patient.getPatientPassword());
		existingPatient.setPatientAddress(patient.getPatientAddress());
		existingPatient.setPatientUsername(patient.getPatientUsername());
		return patientRepository.save(existingPatient);
	}

	public void delete(Integer id) {
		Patient patient = findById(id);
		patientRepository.delete(patient);
	}
}
