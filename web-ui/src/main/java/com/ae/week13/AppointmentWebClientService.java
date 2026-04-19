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
public class AppointmentWebClientService {

	private final RestTemplate restTemplate;
	private final String appointmentServiceBaseUrl;

	public AppointmentWebClientService(RestTemplate restTemplate,
			@Value("${appointment.service.base-url}") String appointmentServiceBaseUrl) {
		this.restTemplate = restTemplate;
		this.appointmentServiceBaseUrl = appointmentServiceBaseUrl;
	}

	public List<Appointment> findAll(String keyword) {
		try {
			URI uri = buildAppointmentsUri(keyword);
			Appointment[] appointments = restTemplate.getForObject(uri, Appointment[].class);
			return appointments == null ? List.of() : Arrays.asList(appointments);
		} catch (HttpStatusCodeException ex) {
			throw buildServiceException(ex, "Unable to load appointments.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Appointment service is not available.");
		}
	}

	public Appointment findById(Integer id) {
		try {
			return restTemplate.getForObject(appointmentServiceBaseUrl + "/api/appointments/{id}", Appointment.class,
					id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Appointment not found: " + id);
			}
			throw buildServiceException(ex, "Unable to load appointment details.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Appointment service is not available.");
		}
	}

	public Appointment save(Appointment appointment) {
		try {
			return restTemplate.postForObject(appointmentServiceBaseUrl + "/api/appointments", appointment,
					Appointment.class);
		} catch (HttpStatusCodeException ex) {
			throw buildServiceException(ex, "Unable to save appointment.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Appointment service is not available.");
		}
	}

	public Appointment update(Integer id, Appointment appointment) {
		try {
			restTemplate.put(appointmentServiceBaseUrl + "/api/appointments/{id}", appointment, id);
			return findById(id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Appointment not found: " + id);
			}
			throw buildServiceException(ex, "Unable to update appointment.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Appointment service is not available.");
		}
	}

	public void delete(Integer id) {
		try {
			restTemplate.delete(appointmentServiceBaseUrl + "/api/appointments/{id}", id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 404) {
				throw new NoSuchElementException("Appointment not found: " + id);
			}
			throw buildServiceException(ex, "Unable to delete appointment.");
		} catch (ResourceAccessException ex) {
			throw buildServiceException(ex, "Appointment service is not available.");
		}
	}

	private URI buildAppointmentsUri(String keyword) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appointmentServiceBaseUrl)
				.path("/api/appointments");
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
