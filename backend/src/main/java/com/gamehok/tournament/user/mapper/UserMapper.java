package com.gamehok.tournament.user.mapper;

import com.gamehok.tournament.user.dto.UserResponseDto;
import com.gamehok.tournament.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link User} entity ↔ DTOs.
 * Uses Spring component model for DI compatibility.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "uuid", source = "uuid")
    UserResponseDto toResponseDto(User user);
}
