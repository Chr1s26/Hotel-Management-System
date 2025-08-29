package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.editor.EditorCreateDTO;
import com.project.HotelManagementSystem.dto.editor.EditorDTO;
import com.project.HotelManagementSystem.dto.editor.EditorResponse;
import com.project.HotelManagementSystem.dto.editor.EditorUpdateDTO;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.EditorRepository;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class EditorService {

    @Autowired
    private EditorRepository editorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleRepository roleRepository;

    public EditorCreateDTO createEditor(EditorCreateDTO editorCreateDTO) {
        Optional<Editor> editorOp = editorRepository.findByNameIgnoreCase(editorCreateDTO.getName());
        if (editorOp.isPresent()) {
            throw new DuplicateException("Editor with name " + editorCreateDTO.getName() + " already exists");
        }
        Editor editor = modelMapper.map(editorCreateDTO, Editor.class);
        User user = userRepository.findById(editorCreateDTO.getApp_user_id())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", editorCreateDTO.getApp_user_id()));
        editor.setUser(user);
        user.setRoles(Set.of(roleRepository.findByRoleName("EDITOR").orElseThrow(() -> new ResourceNotFoundException("User", "id", editorCreateDTO.getApp_user_id()))));
        editor.setStatus(StatusType.ACTIVE);
        editor.setCreatedAt(LocalDateTime.now());
        editor.setCreatedBy(authService.getCurrentUser());
        Editor savedEditor = editorRepository.save(editor);
        EditorCreateDTO dto = modelMapper.map(editor, EditorCreateDTO.class);
        dto.setApp_user_id(editor.getUser().getId());
        return dto;
    }

    public EditorUpdateDTO updateEditor(Long id, EditorUpdateDTO editorUpdateDTO) {
        Optional<Editor> editorOp = editorRepository.findByNameIgnoreCaseAndIdNot(editorUpdateDTO.getName(),id);
        if (editorOp.isPresent()) {
            throw new DuplicateException("Editor with name " + editorUpdateDTO.getName() + " already exists");
        }
        Editor savedEditor = editorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Editor","id",id));
        Editor editor = modelMapper.map(editorUpdateDTO, Editor.class);
        savedEditor.setName(editor.getName());
        savedEditor.setPhone(editor.getPhone());
        savedEditor.setDateOfBirth(editor.getDateOfBirth());
        savedEditor.setNationality(editor.getNationality());
        savedEditor.setPassportNumber(editor.getPassportNumber());
        savedEditor.setNationalIdNumber(editor.getNationalIdNumber());
        savedEditor.setEditorType(editor.getEditorType());
        if (editorUpdateDTO.getApp_user_id() != null &&
                (savedEditor.getUser() == null || !savedEditor.getUser().getId().equals(editorUpdateDTO.getApp_user_id()))) {

            User newUser = userRepository.findById(editorUpdateDTO.getApp_user_id())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", editorUpdateDTO.getApp_user_id()));
            savedEditor.setUser(newUser);
        }
        savedEditor.setUpdatedAt(LocalDateTime.now());
        savedEditor.setUpdatedBy(authService.getCurrentUser());
        savedEditor = editorRepository.save(savedEditor);
        EditorUpdateDTO dto = modelMapper.map(savedEditor, EditorUpdateDTO.class);
        if (savedEditor.getUser() != null) dto.setApp_user_id(savedEditor.getUser().getId());
        return dto;
    }

    public void deleteEditor(Long id) {
        Editor editor = editorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Editor","id",id));
        User user = editor.getUser();
        if(user != null) {
            user.setEditor(null);
            editor.setUser(user);
        }
        editorRepository.delete(editor);
    }

    public EditorDTO findEditorById(Long id) {
        Editor editorOp = editorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Editor","id",id));
        return modelMapper.map(editorOp, EditorDTO.class);
    }

    public EditorResponse findAllEditorWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndSortOrder);
        Page<Editor> editorPage = editorRepository.findAll(pageable);
        List<Editor> editorList = editorPage.getContent();
        List<EditorDTO> editorDTOList = editorList.stream().map(editor -> modelMapper.map(editor, EditorDTO.class)).toList();
        EditorResponse editorResponse = new EditorResponse();
        editorResponse.setEditors(editorDTOList);
        editorResponse.setPageNumber(editorPage.getNumber());
        editorResponse.setPageSize(editorPage.getSize());
        editorResponse.setTotalPages(editorPage.getTotalPages());
        editorResponse.setTotalElements(editorPage.getTotalElements());
        editorResponse.setLastPage(editorPage.isLast());
        return editorResponse;
    }

    public List<EditorDTO> findAllEditor(){
        List<Editor> editorList = editorRepository.findAll();
        return editorList.stream().map(editor -> modelMapper.map(editor, EditorDTO.class)).toList();
    }
}
