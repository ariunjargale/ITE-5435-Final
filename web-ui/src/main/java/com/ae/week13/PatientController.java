package com.ae.week13;

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
@RequestMapping("/patients")
public class PatientController {

	private final PatientWebClientService patientWebClientService;

	public PatientController(PatientWebClientService patientWebClientService) {
		this.patientWebClientService = patientWebClientService;
	}

	@GetMapping("/")
	public String patientsRootRedirect() {
		return "redirect:/patients";
	}

	@GetMapping
	public String listPatients(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("patients", patientWebClientService.findAll(keyword));
		model.addAttribute("keyword", keyword == null ? "" : keyword);
		return "patients/list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		if (!model.containsAttribute("patient")) {
			model.addAttribute("patient", new Patient());
		}
		populateFormAttributes(model, "/patients", "Add Patient", "Save Patient");
		return "patients/form";
	}

	@PostMapping
	public String createPatient(@ModelAttribute Patient patient, RedirectAttributes redirectAttributes, Model model) {
		try {
			Patient savedPatient = patientWebClientService.save(patient);
			redirectAttributes.addFlashAttribute("successMessage",
					"Patient saved successfully for id: " + savedPatient.getPatientId());
			return "redirect:/patients";
		} catch (IllegalStateException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			populateFormAttributes(model, "/patients", "Add Patient", "Save Patient");
			return "patients/form";
		}
	}

	@GetMapping("/{id}")
	public String showPatientDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("patient", patientWebClientService.findById(id));
			return "patients/detail";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/patients";
		}
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			if (!model.containsAttribute("patient")) {
				model.addAttribute("patient", patientWebClientService.findById(id));
			}
			populateFormAttributes(model, "/patients/" + id, "Edit Patient", "Update Patient");
			return "patients/form";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/patients";
		}
	}

	@PostMapping("/{id}")
	public String updatePatient(@PathVariable Integer id, @ModelAttribute Patient patient,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			patientWebClientService.update(id, patient);
			redirectAttributes.addFlashAttribute("successMessage", "Patient updated successfully for id: " + id);
			return "redirect:/patients";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/patients";
		} catch (IllegalStateException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			populateFormAttributes(model, "/patients/" + id, "Edit Patient", "Update Patient");
			return "patients/form";
		}
	}

	@PostMapping("/{id}/delete")
	public String deletePatient(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			patientWebClientService.delete(id);
			redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully for id: " + id);
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		return "redirect:/patients";
	}

	private void populateFormAttributes(Model model, String formAction, String pageTitle, String submitLabel) {
		model.addAttribute("formAction", formAction);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("submitLabel", submitLabel);
	}
}
