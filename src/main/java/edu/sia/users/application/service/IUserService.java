package edu.sia.users.application.service;

import edu.sia.users.application.dto.CreateUserDto;
import edu.sia.users.application.dto.UserDto;
import edu.sia.users.domain.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Flux<UserDto> findAll();
    Mono<User> findById(Long id);
    Mono<User> create(CreateUserDto dto);
    Mono<User> update(Long id, User user);
    Mono<Void> delete(Long id);
}
