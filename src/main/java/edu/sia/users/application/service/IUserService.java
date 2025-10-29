package edu.sia.users.application.service;

import edu.sia.users.application.dto.CreateUserDto;
import edu.sia.users.application.dto.UserDto;
import edu.sia.users.domain.entity.User;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDto> findAll();
    Optional<User> findById(Long id);
    User create(CreateUserDto dto);
    User update(Long id, User user);
    void delete(Long id);
}
