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
public class PatientWebClientService {

	private final RestTemplate restTemplate;
	private final String patientServiceBaseUrl;

	public PatientWebClientService(RestTemplate restTemplate,
			@Value("${patient.service.base-url}") String patientServiceBaseUrl) {
		this.restTemplate = restTemplate;
		this.patientServiceBaseUrl = patientServiceBaseUrl;
	}

	public List<Patient> findAll(String keyword) {
		try {
			URI uri = buildPatientsUri(keyword);
			Patient[] patients = restTemplate.getForObject(uri, Patient[].class);
			return patients == null ? List.of() : Arrays.asList(patients);
		} catch (HttpStatusCodeException ex) {
			throw buildServiceException(ex, "Unable to load patients.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Patient service is not available.");
		}
	}

	public Patient findById(Integer id) {
		try {
			return restTemplate.getForObject(patientServiceBaseUrl + "/api/patients/{id}", Patient.class, id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Patient not found: " + id);
			}
			throw buildServiceException(ex, "Unable to load patient details.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Patient service is not available.");
		}
	}

	public Patient save(Patient patient) {
		try {
			return restTemplate.postForObject(patientServiceBaseUrl + "/api/patients", patient, Patient.class);
		} catch (HttpStatusCodeException ex) {
			throw buildServiceException(ex, "Unable to save patient.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Patient service is not available.");
		}
	}

	public Patient update(Integer id, Patient patient) {
		try {
			restTemplate.put(patientServiceBaseUrl + "/api/patients/{id}", patient, id);
			return findById(id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Patient not found: " + id);
			}
			throw buildServiceException(ex, "Unable to update patient.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Patient service is not available.");
		}
	}

	public void delete(Integer id) {
		try {
			restTemplate.delete(patientServiceBaseUrl + "/api/patients/{id}", id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Patient not found: " + id);
			}
			throw buildServiceException(ex, "Unable to delete patient.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Patient service is not available.");
		}
	}

	private URI buildPatientsUri(String keyword) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(patientServiceBaseUrl).path("/api/patients");
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
