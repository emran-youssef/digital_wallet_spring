package com.project.digital_wallet_with_spring.mappers;

import com.project.digital_wallet_with_spring.dtos.user.UserResponseDto;
import com.project.digital_wallet_with_spring.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserResponseDto toDto(User user);

}
