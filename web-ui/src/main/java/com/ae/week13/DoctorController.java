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
@RequestMapping("/doctors")
public class DoctorController {

	private final DoctorWebClientService doctorWebClientService;

	public DoctorController(DoctorWebClientService doctorWebClientService) {
		this.doctorWebClientService = doctorWebClientService;
	}

	@GetMapping("/")
	public String doctorsRootRedirect() {
		return "redirect:/doctors";
	}

	@GetMapping
	public String listDoctors(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("doctors", doctorWebClientService.findAll(keyword));
		model.addAttribute("keyword", keyword == null ? "" : keyword);
		return "doctors/list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		if (!model.containsAttribute("doctor")) {
			model.addAttribute("doctor", new Doctor());
		}
		populateFormAttributes(model, "/doctors", "Add Doctor", "Save Doctor");
		return "doctors/form";
	}

	@PostMapping
	public String createDoctor(@ModelAttribute Doctor doctor, RedirectAttributes redirectAttributes, Model model) {
		try {
			Doctor savedDoctor = doctorWebClientService.save(doctor);
			redirectAttributes.addFlashAttribute("successMessage",
					"Doctor saved successfully for id: " + savedDoctor.getDoctorsId());
			return "redirect:/doctors";
		} catch (IllegalStateException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			populateFormAttributes(model, "/doctors", "Add Doctor", "Save Doctor");
			return "doctors/form";
		}
	}

	@GetMapping("/{id}")
	public String showDoctorDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("doctor", doctorWebClientService.findById(id));
			return "doctors/detail";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/doctors";
		}
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			if (!model.containsAttribute("doctor")) {
				model.addAttribute("doctor", doctorWebClientService.findById(id));
			}
			populateFormAttributes(model, "/doctors/" + id, "Edit Doctor", "Update Doctor");
			return "doctors/form";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/doctors";
		}
	}

	@PostMapping("/{id}")
	public String updateDoctor(@PathVariable Integer id, @ModelAttribute Doctor doctor,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			doctorWebClientService.update(id, doctor);
			redirectAttributes.addFlashAttribute("successMessage", "Doctor updated successfully for id: " + id);
			return "redirect:/doctors";
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/doctors";
		} catch (IllegalStateException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			populateFormAttributes(model, "/doctors/" + id, "Edit Doctor", "Update Doctor");
			return "doctors/form";
		}
	}

	@PostMapping("/{id}/delete")
	public String deleteDoctor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			doctorWebClientService.delete(id);
			redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully for id: " + id);
		} catch (NoSuchElementException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		return "redirect:/doctors";
	}

	private void populateFormAttributes(Model model, String formAction, String pageTitle, String submitLabel) {
		model.addAttribute("formAction", formAction);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("submitLabel", submitLabel);
	}
}
