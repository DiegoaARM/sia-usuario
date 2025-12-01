package edu.sia.users.infrastructure.controller;

import edu.sia.users.application.dto.CreateUserDto;
import edu.sia.users.application.dto.UserDto;
import edu.sia.users.domain.entity.User;
import edu.sia.users.application.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final IUserService IUserService;

    public UserController(IUserService IUserService) {
        this.IUserService = IUserService;
    }

    @GetMapping
    public Flux<UserDto> getAll() {
        return IUserService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> getById(@PathVariable Long id) {
        return IUserService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole(\"SYSTEM_USER_ADMINISTRATOR\")")
    public Mono<User> create(@RequestBody CreateUserDto dto) {
        return IUserService.create(dto);
    }

    @PutMapping("/{id}")
    public Mono<User> update(@PathVariable Long id, @RequestBody User user) {
        return IUserService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return IUserService.delete(id);
    }
}
