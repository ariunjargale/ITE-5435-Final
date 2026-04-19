package com.ae.week13;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class DoctorService {

	private final DoctorRepository doctorRepository;

	public DoctorService(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}

	public List<Doctor> findAll() {
		return doctorRepository.findAll();
	}

	public Doctor findById(Integer id) {
		return doctorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Doctor not found: " + id));
	}

	public List<Doctor> search(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return findAll();
		}

		String trimmedKeyword = keyword.trim();
		return doctorRepository
				.findByDoctorsNameContainingIgnoreCaseOrDoctorsMobileContainingIgnoreCaseOrDoctorsEmailContainingIgnoreCaseOrDoctorsAddressContainingIgnoreCaseOrDoctorsUsernameContainingIgnoreCase(
						trimmedKeyword, trimmedKeyword, trimmedKeyword, trimmedKeyword, trimmedKeyword);
	}

	public Doctor create(Doctor doctor) {
		doctor.setDoctorsId(null);
		return doctorRepository.save(doctor);
	}

	public Doctor update(Integer id, Doctor doctor) {
		Doctor existingDoctor = findById(id);
		existingDoctor.setDoctorsName(doctor.getDoctorsName());
		existingDoctor.setDoctorsMobile(doctor.getDoctorsMobile());
		existingDoctor.setDoctorsEmail(doctor.getDoctorsEmail());
		existingDoctor.setDoctorsAddress(doctor.getDoctorsAddress());
		existingDoctor.setDoctorsPassword(doctor.getDoctorsPassword());
		existingDoctor.setDoctorsUsername(doctor.getDoctorsUsername());
		return doctorRepository.save(existingDoctor);
	}

	public void delete(Integer id) {
		Doctor doctor = findById(id);
		doctorRepository.delete(doctor);
	}
}
