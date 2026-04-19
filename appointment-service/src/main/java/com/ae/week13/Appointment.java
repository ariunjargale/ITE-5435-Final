package com.ae.week13;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;

	@Column(name = "appointment_number", nullable = false, length = 50)
	private String appointmentNumber;

	@Column(name = "appointment_type", nullable = false, length = 100)
	private String appointmentType;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "appointment_date")
	private LocalDate appointmentDate;

	@Column(name = "appointment_description", length = 255)
	private String appointmentDescription;

	@Column(name = "appointment_doctor_id")
	private Integer appointmentDoctorId;

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getAppointmentNumber() {
		return appointmentNumber;
	}

	public void setAppointmentNumber(String appointmentNumber) {
		this.appointmentNumber = appointmentNumber;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentDescription() {
		return appointmentDescription;
	}

	public void setAppointmentDescription(String appointmentDescription) {
		this.appointmentDescription = appointmentDescription;
	}

	public Integer getAppointmentDoctorId() {
		return appointmentDoctorId;
	}

	public void setAppointmentDoctorId(Integer appointmentDoctorId) {
		this.appointmentDoctorId = appointmentDoctorId;
	}
}
