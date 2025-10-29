package edu.sia.users.application.mapper;

import edu.sia.users.application.dto.UserDto;
import edu.sia.users.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mapping(target = "birthdate", source = "birthdate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "lastLogin", source = "lastLogin", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    UserDto toDto(User user);

    @Mapping(target = "birthdate", source = "birthdate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "lastLogin", source = "lastLogin", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "tenantId", ignore = true)
    User toEntity(UserDto userDto);
}
