package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.editor.EditorCreateDTO;
import com.project.HotelManagementSystem.dto.editor.EditorDTO;
import com.project.HotelManagementSystem.dto.editor.EditorResponse;
import com.project.HotelManagementSystem.dto.editor.EditorUpdateDTO;
import com.project.HotelManagementSystem.service.EditorService;
import com.project.HotelManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/editors")
public class EditorController {
    @Autowired
    private EditorService editorService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllEditors(Model model,
                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                                @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {
        EditorResponse editorResponse = editorService.findAllEditorWithPagination(pageNumber, pageSize, sortBy, sortOrder);
        List<EditorDTO> editors = editorResponse.getEditors();
        model.addAttribute("editors", editors);
        model.addAttribute("response", editorResponse);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
        return "editors/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("editor", new EditorCreateDTO());
        model.addAttribute("users", userService.findAllUsers());
        return "editors/create";
    }

    @PostMapping("/create")
    public String createEditor(@Valid @ModelAttribute("editor") EditorCreateDTO editorCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "editors/create";
        }
        editorService.createEditor(editorCreateDTO);
        return "redirect:/editors";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("editor", editorService.findEditorById(id));
        model.addAttribute("users", userService.findAllUsers());
        return "editors/edit";
    }

    @PostMapping("/update/{id}")
    public String updateEditor(@PathVariable Long id, @Valid @ModelAttribute("editor") EditorUpdateDTO editorUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editor", editorService.findEditorById(id));
            model.addAttribute("users", userService.findAllUsers());
            return "editors/edit";
        }
        editorService.updateEditor(id, editorUpdateDTO);
        return "redirect:/editors";
    }

    @GetMapping("/delete/{id}")
    public String deleteEditor(@PathVariable Long id) {
        editorService.deleteEditor(id);
        return "redirect:/editors";
    }
}
