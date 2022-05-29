package kr.or.cola.backend.todo.item;

import kr.or.cola.backend.todo.dto.ProgressDto;
import kr.or.cola.backend.todo.folder.domain.Folder;
import kr.or.cola.backend.todo.item.dto.ItemsResponseDto;
import kr.or.cola.backend.todo.item.domain.Item;
import kr.or.cola.backend.todo.item.domain.ItemRepository;
import kr.or.cola.backend.todo.item.dto.ItemDto;
import kr.or.cola.backend.todo.item.dto.ItemCreateOrUpdateRequestDto;
import kr.or.cola.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // Create or Update
    public void createOrUpdateItems(ItemCreateOrUpdateRequestDto requestDto) {
        requestDto.getItemDtos().forEach(itemDto -> {
            Item item = itemRepository.findByDateAndFolderId(itemDto.getDate(), itemDto.getFolderId());
            if (item == null) {
                item =  Item.builder()
                        .date(itemDto.getDate())
                        .folderId(itemDto.getFolderId())
                        .todos(itemDto.getTodos())
                        .progress(itemDto.getProgress())
                        .build();
            } else {
                item.update(itemDto.getDate(), itemDto.getFolderId(), itemDto.getTodos(), itemDto.getProgress());
            }
            itemRepository.save(item);
        });
    }

    // Read
    public List<ItemDto> readItems(Long userId, LocalDate date) {
        List<Long> folderIds = userRepository.findById(userId).orElseThrow().getFolderOrder();
        return itemRepository.findAllByDateAndFolderIdIn(date, folderIds).stream()
                .map(ItemDto::new).collect(Collectors.toList());
    }

    public ItemDto readItem(LocalDate date, Long folderId) {
        return new ItemDto(itemRepository.findByDateAndFolderId(date, folderId));
    }

    // Delete
    public void deleteAll(Long folderId) {
        itemRepository.deleteAllByFolderId(folderId);
    }
}
