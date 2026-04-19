package com.ae.week13;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	public AppointmentService(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

	public Appointment findById(Integer id) {
		return appointmentRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Appointment not found: " + id));
	}

	public List<Appointment> search(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return findAll();
		}

		String trimmedKeyword = keyword.trim();
		return appointmentRepository
				.findByAppointmentNumberContainingIgnoreCaseOrAppointmentTypeContainingIgnoreCaseOrAppointmentDescriptionContainingIgnoreCase(
						trimmedKeyword, trimmedKeyword, trimmedKeyword);
	}

	public Appointment create(Appointment appointment) {
		appointment.setAppointmentId(null);
		return appointmentRepository.save(appointment);
	}

	public Appointment update(Integer id, Appointment appointment) {
		Appointment existingAppointment = findById(id);
		existingAppointment.setAppointmentNumber(appointment.getAppointmentNumber());
		existingAppointment.setAppointmentType(appointment.getAppointmentType());
		existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
		existingAppointment.setAppointmentDescription(appointment.getAppointmentDescription());
		existingAppointment.setAppointmentDoctorId(appointment.getAppointmentDoctorId());
		return appointmentRepository.save(existingAppointment);
	}

	public void delete(Integer id) {
		Appointment appointment = findById(id);
		appointmentRepository.delete(appointment);
	}
}
