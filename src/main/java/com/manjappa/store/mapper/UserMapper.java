package com.manjappa.store.mapper;

import com.manjappa.store.dtos.*;
import com.manjappa.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    //This createdAt field mapped at UserDto

    UserDto toDto(User user);
    User toUserEntity(UserRegisterDto userRequestDto);
    User  update(UpdateUserDto updateUserDto,@MappingTarget User user);
    User toLoginEntity(LoginDto loginUserDto);
}
