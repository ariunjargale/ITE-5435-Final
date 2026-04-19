package com.ae.week13;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor")
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doctors_id", nullable = false)
	private Integer doctorsId;

	@Column(name = "doctors_name", nullable = false, length = 100)
	private String doctorsName;

	@Column(name = "doctors_mobile", nullable = false, length = 30)
	private String doctorsMobile;

	@Column(name = "doctors_email", nullable = false, length = 100)
	private String doctorsEmail;

	@Column(name = "doctors_address", length = 255)
	private String doctorsAddress;

	@Column(name = "doctors_password", nullable = false, length = 100)
	private String doctorsPassword;

	@Column(name = "doctors_username", nullable = false, length = 50)
	private String doctorsUsername;

	public Integer getDoctorsId() {
		return doctorsId;
	}

	public void setDoctorsId(Integer doctorsId) {
		this.doctorsId = doctorsId;
	}

	public String getDoctorsName() {
		return doctorsName;
	}

	public void setDoctorsName(String doctorsName) {
		this.doctorsName = doctorsName;
	}

	public String getDoctorsMobile() {
		return doctorsMobile;
	}

	public void setDoctorsMobile(String doctorsMobile) {
		this.doctorsMobile = doctorsMobile;
	}

	public String getDoctorsEmail() {
		return doctorsEmail;
	}

	public void setDoctorsEmail(String doctorsEmail) {
		this.doctorsEmail = doctorsEmail;
	}

	public String getDoctorsAddress() {
		return doctorsAddress;
	}

	public void setDoctorsAddress(String doctorsAddress) {
		this.doctorsAddress = doctorsAddress;
	}

	public String getDoctorsPassword() {
		return doctorsPassword;
	}

	public void setDoctorsPassword(String doctorsPassword) {
		this.doctorsPassword = doctorsPassword;
	}

	public String getDoctorsUsername() {
		return doctorsUsername;
	}

	public void setDoctorsUsername(String doctorsUsername) {
		this.doctorsUsername = doctorsUsername;
	}
}
