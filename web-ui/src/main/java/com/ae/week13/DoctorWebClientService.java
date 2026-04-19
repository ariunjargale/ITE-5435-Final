package com.ae.week13;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DoctorWebClientService {

	private final RestTemplate restTemplate;
	private final String doctorServiceBaseUrl;

	public DoctorWebClientService(RestTemplate restTemplate,
			@Value("${doctor.service.base-url}") String doctorServiceBaseUrl) {
		this.restTemplate = restTemplate;
		this.doctorServiceBaseUrl = doctorServiceBaseUrl;
	}

	public List<Doctor> findAll(String keyword) {
		try {
			URI uri = buildDoctorsUri(keyword);
			Doctor[] doctors = restTemplate.getForObject(uri, Doctor[].class);
			return doctors == null ? List.of() : Arrays.asList(doctors);
		} catch (HttpStatusCodeException ex) {
			throw buildServiceException(ex, "Unable to load doctors.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Doctor service is not available.");
		}
	}

	public Doctor findById(Integer id) {
		try {
			return restTemplate.getForObject(doctorServiceBaseUrl + "/api/doctors/{id}", Doctor.class, id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Doctor not found: " + id);
			}
			throw buildServiceException(ex, "Unable to load doctor details.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Doctor service is not available.");
		}
	}

	public Doctor save(Doctor doctor) {
		try {
			return restTemplate.postForObject(doctorServiceBaseUrl + "/api/doctors", doctor, Doctor.class);
		} catch (HttpStatusCodeException ex) {
			throw buildServiceException(ex, "Unable to save doctor.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Doctor service is not available.");
		}
	}

	public Doctor update(Integer id, Doctor doctor) {
		try {
			restTemplate.put(doctorServiceBaseUrl + "/api/doctors/{id}", doctor, id);
			return findById(id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Doctor not found: " + id);
			}
			throw buildServiceException(ex, "Unable to update doctor.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Doctor service is not available.");
		}
	}

	public void delete(Integer id) {
		try {
			restTemplate.delete(doctorServiceBaseUrl + "/api/doctors/{id}", id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Doctor not found: " + id);
			}
			throw buildServiceException(ex, "Unable to delete doctor.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Doctor service is not available.");
		}
	}

	private URI buildDoctorsUri(String keyword) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(doctorServiceBaseUrl).path("/api/doctors");
		if (keyword != null && !keyword.isBlank()) {
			builder.queryParam("keyword", keyword);
		}
		return builder.build().encode().toUri();
	}

	private IllegalStateException buildServiceException(HttpStatusCodeException ex, String fallbackMessage) {
		String responseBody = ex.getResponseBodyAsString();
		String message = (responseBody == null || responseBody.isBlank()) ? fallbackMessage : responseBody;
		return new IllegalStateException(message, ex);
	}

	private IllegalStateException buildServiceException(ResourceAccessException ex, String fallbackMessage) {
		return new IllegalStateException(fallbackMessage, ex);
	}
}
