package com.ae.week13;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	List<Appointment> findByAppointmentNumberContainingIgnoreCaseOrAppointmentTypeContainingIgnoreCaseOrAppointmentDescriptionContainingIgnoreCase(
			String appointmentNumber, String appointmentType, String appointmentDescription);
}
