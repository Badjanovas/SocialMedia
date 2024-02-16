package com.socialMedia.service.mappingService;

import com.socialMedia.dto.UserRequestDTO;
import com.socialMedia.dto.UserResponseDTO;
import com.socialMedia.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMappingService {

    public User mapToEntity(final UserRequestDTO requestDto) {
        return User.builder()
                .name(requestDto.getName())
                .age(requestDto.getAge())
                .build();
    }

    public List<UserResponseDTO> mapToResponse(List<User> allUsers){
        List<UserResponseDTO> mappedUsers = new ArrayList<>();
        for (User user : allUsers) {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setUserName(user.getName());
            dto.setUserAge(user.getAge());
            dto.setIsAdult(user.getAge()>=18);
            mappedUsers.add(dto);
        }
        return mappedUsers;
    }

}
