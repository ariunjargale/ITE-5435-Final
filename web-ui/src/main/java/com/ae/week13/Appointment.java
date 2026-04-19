package com.ae.week13;

import java.time.LocalDate;

public class Appointment {

	private Integer appointmentId;
	private String appointmentNumber;
	private String appointmentType;
	private LocalDate appointmentDate;
	private String appointmentDescription;
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
