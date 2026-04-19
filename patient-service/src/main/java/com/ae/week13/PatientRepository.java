package com.ae.week13;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

	List<Patient> findByPatientMobileContainingIgnoreCaseOrPatientEmailContainingIgnoreCaseOrPatientNameContainingIgnoreCaseOrPatientAddressContainingIgnoreCaseOrPatientUsernameContainingIgnoreCase(
			String patientMobile, String patientEmail, String patientName, String patientAddress,
			String patientUsername);
}
