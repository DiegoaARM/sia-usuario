package edu.sia.users.application.service;

import edu.sia.role.Role;
import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.tenant.domain.repository.TenantRepository;
import edu.sia.users.application.dto.CreateUserDto;
import edu.sia.users.application.dto.UserDto;
import edu.sia.users.application.factory.UserFactory;
import edu.sia.users.application.mapper.IUserMapper;
import edu.sia.users.domain.entity.User;
import edu.sia.users.domain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserService implements IUserService {

    private final IUserRepository IUserRepository;
    private final IIdentityService identityService;
    private final UserFactory userFactory;
    private final TenantRepository tenantRepository;
    private final IUserMapper userMapper;

    @Autowired
    public UserService(IUserRepository IUserRepository, IIdentityService identityService,
                       UserFactory userFactory, TenantRepository tenantRepository, IUserMapper userMapper) {
        this.IUserRepository = IUserRepository;
        this.identityService = identityService;
        this.userFactory = userFactory;
        this.tenantRepository = tenantRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Flux<UserDto> findAll() {
        return IUserRepository.findAll()
                .map(userMapper::toDto);
    }

    @Override
    public Mono<User> findById(Long id) {
        return IUserRepository.findById(id);
    }

    @Override
    public Mono<User> create(CreateUserDto dto) {
        Role role = Role.fromValue(dto.roleName());

        if(!(role.equals(Role.STUDENT) || role.equals(Role.TEACHER)))
            return Mono.error(new IllegalArgumentException("No puede crear un usuario con el rol: " + dto.roleName()));

        // Buscar el tenant y crear usuario reactivamente
        return tenantRepository.findById(dto.tenantId())
                .switchIfEmpty(Mono.error(new RuntimeException("Tenant no encontrado con id: " + dto.tenantId())))
                .flatMap(tenant -> {
                    // Crear usuario en el proveedor de identidad (Cognito) - Nota: esto es bloqueante
                    // Lo ejecutamos en un scheduler dedicado para operaciones bloqueantes
                    return Mono.fromCallable(() -> identityService.createUser(
                            dto.email(),
                            dto.givenName(),
                            dto.familyName(),
                            dto.roleName(),
                            dto.idNumber()
                    ))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(cognitoUserId -> {
                        // Crear entidad User usando el factory
                        User user = userFactory.createFromDto(cognitoUserId, tenant, dto);
                        // Guardar en la base de datos
                        return IUserRepository.save(user);
                    });
                });
    }

    @Override
    public Mono<User> update(Long id, User user) {
        return IUserRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con id: " + id)))
                .flatMap(existing -> {
                    user.setUsuarioId(existing.getUsuarioId());
                    return IUserRepository.save(user);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return IUserRepository.deleteById(id);
    }
}