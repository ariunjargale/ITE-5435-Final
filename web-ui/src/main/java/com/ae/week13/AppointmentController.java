package com.ae.week13;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

	private final AppointmentWebClientService appointmentWebClientService;
	private final DoctorWebClientService doctorWebClientService;

	public AppointmentController(AppointmentWebClientService appointmentWebClientService,
			DoctorWebClientService doctorWebClientService) {
		this.appointmentWebClientService = appointmentWebClientService;
		this.doctorWebClientService = doctorWebClientService;
	}

	@GetMapping("/")
	public String appointmentsRootRedirect() {
		return "redirect:/appointments";
	}

	@GetMapping
	public String listAppointments(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("appointments", appointmentWebClientService.findAll(keyword));
		model.addAttribute("keyword", keyword == null ? "" : keyword);
		return "appointments/list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		if (!model.containsAttribute("appointment")) {
			model.addAttribute("appointment", new Appointment());
		}
		populateFormAttributes(model, "/appointments", "Add Appointment", "Save Appointment");
		populateDoctorOptions(model);
		return "appointments/form";
	}

	@PostMapping
	public String createAppointment(@ModelAttribute Appointment appointment, RedirectAttributes redirectAttributes,
			Model model) {
		try {
			Appointment savedAppointment = appointmentWebClientService.save(appointment);
			redirectAttributes.addFlashAttribute("successMessage",
					"Appointment saved successfully for id: " + savedAppointment.getAppointmentId());
			return "redirect:/appointments";
		} catch (IllegalStateException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			populateFormAttributes(model, "/appointments", "Add Appointment", "Save Appointment");
			populateDoctorOptions(model);
			return "appointments/form";
		}
	}

	@GetMapping("/{id}")
	public String showAppointmentDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("appointment", appointmentWebClientService.findById(id));
			return "appointments/detail";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/appointments";
		}
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			if (!model.containsAttribute("appointment")) {
				model.addAttribute("appointment", appointmentWebClientService.findById(id));
			}
			populateFormAttributes(model, "/appointments/" + id, "Edit Appointment", "Update Appointment");
			populateDoctorOptions(model);
			return "appointments/form";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/appointments";
		}
	}

	@PostMapping("/{id}")
	public String updateAppointment(@PathVariable Integer id, @ModelAttribute Appointment appointment,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			appointmentWebClientService.update(id, appointment);
			redirectAttributes.addFlashAttribute("successMessage", "Appointment updated successfully for id: " + id);
			return "redirect:/appointments";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/appointments";
		} catch (IllegalStateException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			populateFormAttributes(model, "/appointments/" + id, "Edit Appointment", "Update Appointment");
			populateDoctorOptions(model);
			return "appointments/form";
		}
	}

	@PostMapping("/{id}/delete")
	public String deleteAppointment(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			appointmentWebClientService.delete(id);
			redirectAttributes.addFlashAttribute("successMessage", "Appointment deleted successfully for id: " + id);
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		return "redirect:/appointments";
	}

	private void populateFormAttributes(Model model, String formAction, String pageTitle, String submitLabel) {
		model.addAttribute("formAction", formAction);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("submitLabel", submitLabel);
	}

	private void populateDoctorOptions(Model model) {
		try {
			List<Doctor> doctors = doctorWebClientService.findAll(null);
			model.addAttribute("doctors", doctors);
		} catch (IllegalStateException ex) {
			model.addAttribute("doctors", List.of());
			model.addAttribute("doctorLoadMessage",
					"Doctor list could not be loaded. You can still save without a doctor.");
		}
	}
}
