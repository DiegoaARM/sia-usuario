package edu.sia.users.application.service;

import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.tenant.domain.repository.TenantRepository;
import edu.sia.users.application.dto.CreateUserDto;
import edu.sia.users.application.factory.UserFactory;
import edu.sia.users.domain.entity.User;
import edu.sia.users.domain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository IUserRepository;
    private final IIdentityService identityService;
    private final UserFactory userFactory;
    private final TenantRepository tenantRepository;

    @Autowired
    public UserService(IUserRepository IUserRepository, IIdentityService identityService,
                      UserFactory userFactory, TenantRepository tenantRepository) {
        this.IUserRepository = IUserRepository;
        this.identityService = identityService;
        this.userFactory = userFactory;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public List<User> findAll() {
        return IUserRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return IUserRepository.findById(id);
    }

    @Override
    public User create(CreateUserDto dto) {
        // Buscar el tenant
        Tenant tenant = tenantRepository.findById(dto.tenantId())
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado con id: " + dto.tenantId()));

        // Crear usuario en el proveedor de identidad (Cognito)
        String cognitoUserId = identityService.createUser(
                dto.email(),
                dto.givenName(),
                dto.familyName(),
                dto.roleName(),
                dto.idNumber()
        );

        // Crear entidad User usando el factory
        User user = userFactory.createFromDto(cognitoUserId, tenant, dto);

        // Guardar en la base de datos
        return IUserRepository.save(user);

    }

    @Override
    public User update(Long id, User user) {
        return IUserRepository.findById(id)
                .map(existing -> {
                    user.setUsuarioId(existing.getUsuarioId());
                    return IUserRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public void delete(Long id) {
        IUserRepository.deleteById(id);
    }
}