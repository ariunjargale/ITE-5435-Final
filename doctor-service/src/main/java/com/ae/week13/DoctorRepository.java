package com.ae.week13;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

	List<Doctor> findByDoctorsNameContainingIgnoreCaseOrDoctorsMobileContainingIgnoreCaseOrDoctorsEmailContainingIgnoreCaseOrDoctorsAddressContainingIgnoreCaseOrDoctorsUsernameContainingIgnoreCase(
			String doctorsName, String doctorsMobile, String doctorsEmail, String doctorsAddress,
			String doctorsUsername);
}
