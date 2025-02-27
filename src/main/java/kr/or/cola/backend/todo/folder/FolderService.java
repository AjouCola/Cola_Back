package kr.or.cola.backend.todo.folder;

import kr.or.cola.backend.todo.folder.domain.Folder;
import kr.or.cola.backend.todo.folder.domain.FolderRepository;
import kr.or.cola.backend.todo.folder.dto.FolderUpdateRequestDto;
import kr.or.cola.backend.todo.folder.dto.FolderResponseDto;
import kr.or.cola.backend.user.domain.User;
import kr.or.cola.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    // Create
    public Long createFolder(Long userId, FolderUpdateRequestDto requestDto) {
        User user = findUserById(userId);

        Folder folder = Folder.builder()
                .user(user)
                .name(requestDto.getName())
                .color(requestDto.getColor())
                .build();
        Long folderId = folderRepository.save(folder).getFolderId();
        user.addFolder(folderId);
        userRepository.save(user);
        return folderId;
    }

    // Read
    public List<FolderResponseDto> readFolders(Long userId) {
        return folderRepository.findAllByUserId(userId).stream()
            .map(FolderResponseDto::new)
            .collect(Collectors.toList());
    }

    public FolderResponseDto readFolder(Long folderId) {
        return new FolderResponseDto(findFolderById(folderId));
    }

    // Update
    public void updateFolder(Long folderId, FolderUpdateRequestDto requestDto) {
        Folder folder = folderRepository.getById(folderId);
        folder.update(requestDto.getName(), requestDto.getColor());
        folderRepository.save(folder);
    }

    // Delete
    public void deleteAll(Long userId) {
        List<Long> ids = folderRepository.findAllByUserId(userId).stream()
            .map(Folder::getFolderId)
            .collect(Collectors.toList());
        deleteAllFolder(ids);
    }

    public void deleteAllFolder(List<Long> folderIds) {
        folderRepository.deleteAllById(folderIds);
    }

    public void deleteFolder(Long folderId) {
        folderRepository.deleteById(folderId);
    }

    /**
     * 중복 함수 => todo : user service로 이동 필요
     * @param userId
     * @return
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid User ID: id=" + userId));
    }

    private Folder findFolderById(Long folderId) {
        return folderRepository.findById(folderId)
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid Folder id: id=" + folderId));
    }
}
