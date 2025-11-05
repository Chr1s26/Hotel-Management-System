package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.editor.*;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.EditorSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
            throw new DuplicateException("editor",editorCreateDTO,"name","editors/create","An editor with the same name already exists");
        }
        validateIdsUniqueOrThrow(null, editorCreateDTO.getPassportNumber(), editorCreateDTO.getNationalIdNumber());

        Editor editor = modelMapper.map(editorCreateDTO, Editor.class);

        User user = userRepository.findById(editorCreateDTO.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("editor",editorCreateDTO,"id","editors/create","An editor with the id cannot be found"));
        Role editorRole = roleRepository.findByRoleName("EDITOR")
                .orElseThrow(() -> new ResourceNotFoundException("editor",editorCreateDTO,"id","editors/create","An editor with the id cannot be found"));

        if(user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(editorRole);
        editor.setUser(user);
        editor.setStatus(StatusType.ACTIVE);
        editor.setCreatedAt(LocalDateTime.now());
        editor.setCreatedBy(authService.getCurrentUser());
        editor = editorRepository.save(editor);
        EditorCreateDTO dto = toCreateDTO(editor);
        return dto;
    }

    public EditorUpdateDTO updateEditor(Long id, EditorUpdateDTO editorUpdateDTO) {
        Optional<Editor> editorOp = editorRepository.findByNameIgnoreCaseAndIdNot(editorUpdateDTO.getName(),id);
        if (editorOp.isPresent()) {
            throw new DuplicateException("editor",editorUpdateDTO,"name","editors/edit","An editor with the same name already exists");
        }
        validateIdsUniqueOrThrow(id, editorUpdateDTO.getPassportNumber(), editorUpdateDTO.getNationalIdNumber());
//        Long userId = editorUpdateDTO.getUser();
        Editor savedEditor = editorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("editor",editorUpdateDTO,"id","editors/edit","An editor with the id cannot be found"));
        Editor editor = modelMapper.map(editorUpdateDTO, Editor.class);
        savedEditor.setName(editor.getName());
        savedEditor.setPhone(editor.getPhone());
        savedEditor.setDateOfBirth(editor.getDateOfBirth());
        savedEditor.setNationality(editor.getNationality());
        savedEditor.setPassportNumber(editor.getPassportNumber());
        savedEditor.setNationalIdNumber(editor.getNationalIdNumber());
        savedEditor.setEditorType(editor.getEditorType());
        if (editorUpdateDTO.getUser() != null &&
                (savedEditor.getUser() == null || !savedEditor.getUser().getId().equals(editorUpdateDTO.getUser()))) {

            User newUser = userRepository.findById(editorUpdateDTO.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("editor",editorUpdateDTO,"id","editors/edit","An editor with the id cannot be found"));
            savedEditor.setUser(newUser);
        }
        savedEditor.setUpdatedAt(LocalDateTime.now());
        savedEditor.setUpdatedBy(authService.getCurrentUser());
        savedEditor = editorRepository.save(savedEditor);
        EditorUpdateDTO dto = toUpdateDTO(savedEditor);
        return dto;
    }

    public void deleteEditor(Long id) {
        Optional<Editor> editorOptional = editorRepository.findById(id);
        if(editorOptional.isEmpty()){
            throw new ResourceNotFoundException("editor",editorOptional,"id","editors","An editor with the id cannot be found");
        }
        Editor editor = editorOptional.get();
        User user = editor.getUser();
        if(user != null) {
            Role editorRole = roleRepository.findByRoleName("EDITOR").orElse(null);
            if(user.getRoles() != null) {
                user.getRoles().remove(editorRole);
                userRepository.save(user);
            }
            user.setEditor(null);
            editor.setUser(null);
        }
        editorRepository.delete(editor);
    }

    public EditorDTO findEditorById(Long id) {
        Optional<Editor> editorOp = editorRepository.findById(id);
        if(editorOp.isEmpty()){
            throw new ResourceNotFoundException("editor",editorOp,"id","editors","An editor with the id cannot be found");
        }
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

    public EditorResponse search(EditorSeearchCriteria criteria) {
        Sort sortByAndOrder = criteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(criteria.getSortBy()).ascending() : Sort.by(criteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(criteria.getPageNumber(),criteria.getPageSize(),sortByAndOrder);

        Specification<Editor> spec = Specification.where(EditorSpecification.findByName(criteria.getName()))
                .and(EditorSpecification.findByPhone(criteria.getPhone()))
                .and(EditorSpecification.findByDOB(criteria.getDateOfBirth()))
                .and(EditorSpecification.findByNationality(criteria.getNationality()))
                .and(EditorSpecification.findByPassportNumber(criteria.getPassportNumber()))
                .and(EditorSpecification.findByNationalIdNumber(criteria.getNationalIdNumber()))
                .and(EditorSpecification.findByEditorType(criteria.getEditorType()))
                .and(EditorSpecification.findByStatus(criteria.getStatus()));

        Page<Editor> editorPage = editorRepository.findAll(spec,pageable);
        List<Editor> editors = editorPage.getContent();
        List<EditorDTO> editorDTOList = editors.stream().map(editor -> modelMapper.map(editor, EditorDTO.class)).toList();
        EditorResponse editorResponse = new EditorResponse();
        editorResponse.setEditors(editorDTOList);
        editorResponse.setPageNumber(editorPage.getNumber());
        editorResponse.setPageSize(editorPage.getSize());
        editorResponse.setTotalPages(editorPage.getTotalPages());
        editorResponse.setTotalElements(editorPage.getTotalElements());
        editorResponse.setLastPage(editorPage.isLast());
        return editorResponse;
    }

    private void validateIdsUniqueOrThrow(Long currentEditorId, String passport, String nationalId) {
        String p = (passport == null) ? null : passport.trim();
        String n = (nationalId == null) ? null : nationalId.trim();

        if ((p == null || p.isEmpty()) && (n == null || n.isEmpty())) {
            throw new IllegalArgumentException("Either passport number or national ID number must be provided.");
        }

        if (p != null && !p.isEmpty()) {
            boolean dup = (currentEditorId == null)
                    ? editorRepository.existsByPassportNumberIgnoreCase(p)
                    : editorRepository.existsByPassportNumberIgnoreCaseAndIdNot(p, currentEditorId);
//            if (dup) {
//                throw new DuplicateException("Passport number is already used by another editor.");
//            }
        }

        if (n != null && !n.isEmpty()) {
            boolean dup = (currentEditorId == null)
                    ? editorRepository.existsByNationalIdNumberIgnoreCase(n)
                    : editorRepository.existsByNationalIdNumberIgnoreCaseAndIdNot(n, currentEditorId);
//            if (dup) {
//                throw new DuplicateException("National ID number is already used by another editor.");
//            }
        }
    }

    public EditorCreateDTO toCreateDTO(Editor editor) {
        EditorCreateDTO dto = new EditorCreateDTO();
        dto.setId(editor.getId());
        dto.setName(editor.getName());
        dto.setPhone(editor.getPhone());
        dto.setDateOfBirth(editor.getDateOfBirth());
        dto.setNationality(editor.getNationality());
        dto.setPassportNumber(editor.getPassportNumber());
        dto.setNationalIdNumber(editor.getNationalIdNumber());
        dto.setEditorType(editor.getEditorType());
        if(editor.getUser() != null){
            dto.setUser(editor.getUser().getId());
        }
        return dto;
    }

    public EditorUpdateDTO toUpdateDTO(Editor editor) {
        EditorUpdateDTO dto = new EditorUpdateDTO();
        dto.setId(editor.getId());
        dto.setName(editor.getName());
        dto.setPhone(editor.getPhone());
        dto.setDateOfBirth(editor.getDateOfBirth());
        dto.setNationality(editor.getNationality());
        dto.setPassportNumber(editor.getPassportNumber());
        dto.setNationalIdNumber(editor.getNationalIdNumber());
        dto.setEditorType(editor.getEditorType());
        if(editor.getUser() != null){
            dto.setUser(editor.getUser().getId());
        }
        return dto;
    }

}
