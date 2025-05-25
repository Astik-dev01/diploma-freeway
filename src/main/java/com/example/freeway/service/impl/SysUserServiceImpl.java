package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.*;
import com.example.freeway.db.repository.*;
import com.example.freeway.db.repository.specification.SysUserSpecification;
import com.example.freeway.exception.BadRequestException;
import com.example.freeway.exception.NotFoundException;
import com.example.freeway.exception.TooManyRequestsException;
import com.example.freeway.model.user.filter.UserFilterDto;
import com.example.freeway.model.user.request.AdminChangePasswordRequestDto;
import com.example.freeway.model.user.request.ResetPasswordRequestDto;
import com.example.freeway.model.user.request.SysUserChangePasswordRequestDto;
import com.example.freeway.model.user.request.SysUserRequest;
import com.example.freeway.model.user.response.PageSysUserDtoResponse;
import com.example.freeway.model.user.response.SysUserResponseDto;
import com.example.freeway.service.SysLogRequestService;
import com.example.freeway.service.SysUserService;
import com.example.freeway.util.GsonConfig;
import com.example.freeway.util.CustomMailSender;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserRepository repository;
    private final BaseController baseController;
    private final SysRoleRepository rolesRepository;
    private final SysLogRequestService logService;
    private final CustomMailSender customMailSender;
    private final EmailVerificationService emailVerificationService;

    private final FacultyRepository facultyRepository;
    private final StudentDetailsRepository studentDetailsRepository;

    private final PasswordResetTokenRepository tokenRepository;

    private final AccountActivationRepository accountActivationRepository;


    private final Gson gson = GsonConfig.createGson();

    @Override
    public PageSysUserDtoResponse findAll(UserFilterDto filter) {
        Specification<SysUser> spec = new SysUserSpecification(filter);
        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize(),
                Sort.by(Sort.Direction.DESC,
                        "createdTime"));
        Page<SysUser> sysUsersPage = repository.findAll(spec, pageable);

        return PageSysUserDtoResponse.from(sysUsersPage);
    }

    @Override
    public SysUser findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("error.user.not_found"));
    }

    @Override
    public Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    @Override
    @Transactional
    public SysUserResponseDto create(SysUserRequest userDto, HttpServletRequest request) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var roles = rolesRepository.findAllById(userDto.getRoleIds());
        if (roles.size() != userDto.getRoleIds().size()) {
            throw new NotFoundException("error.user.not_found.role");
        }
        if (!emailVerificationService.verifyCode(userDto.getEmail(), userDto.getOtpCode())) {
            throw new BadRequestException("Неверный или просроченный код подтверждения");
        }
        Optional<SysUser> userOptional = repository.findByEmail(userDto.getEmail());
        SysUser user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setDeleted(false);
            user.setEmailVerified(false);
            user.setEditedTime(new Date());
        } else {
            user = SysUser.builder()
                    .name(userDto.getName())
                    .secondName(userDto.getSecondName())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .temporaryAccessUntilTime(userDto.getTemporaryAccessUntilTime())
                    .email(userDto.getEmail())
                    .phoneNumber(userDto.getPhone())
                    .birthdate(userDto.getBirthdate())
                    .gender(userDto.getGender())
                    .roles(new HashSet<>(roles))
                    .build();
        }

        user.setRoles(new HashSet<>(roles));

        repository.save(user);
        boolean isStudent = roles.stream()
                .anyMatch(role -> role.getAlias().equals("STUDENT"));
        if (isStudent) {
            if (userDto.getStudentId() != null) {
                Faculty faculty = facultyRepository.findById(userDto.getFacultyId())
                        .orElseThrow(() -> new NotFoundException("Факультет не найден"));

                SysUser advisor = repository.findById(userDto.getAdvisorId())
                        .orElseThrow(() -> new NotFoundException("Advisor не найден"));

                StudentDetails studentDetails = StudentDetails.builder()
                        .user(user)
                        .studentId(userDto.getStudentId())
                        .faculty(faculty)
                        .advisor(advisor)
                        .build();

                studentDetailsRepository.save(studentDetails);
            }
        }

        userDto.setPassword(null);
        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userDto),
                request);
        return SysUserResponseDto.from(user);
    }



    @Override
    @Transactional
    public SysUserResponseDto createUserByAdmin(SysUserRequest userDto, HttpServletRequest request) throws MessagingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var roles = rolesRepository.findAllById(userDto.getRoleIds());
        if (roles.size() != userDto.getRoleIds().size()) {
            throw new NotFoundException("error.user.not_found.role");
        }

        Optional<SysUser> userOptional = repository.findByEmail(userDto.getEmail());
        SysUser user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setDeleted(false);
            user.setEmailVerified(false);
            user.setEditedTime(new Date());
        } else {
            user = SysUser.builder()
                    .name(userDto.getName())
                    .secondName(userDto.getSecondName())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .temporaryAccessUntilTime(userDto.getTemporaryAccessUntilTime())
                    .email(userDto.getEmail())
                    .phoneNumber(userDto.getPhone())
                    .birthdate(userDto.getBirthdate())
                    .gender(userDto.getGender())
                    .roles(new HashSet<>(roles))
                    .build();
        }

        user.setRoles(new HashSet<>(roles));

        repository.save(user);

        userDto.setPassword(null);
        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userDto),
                request);
        return SysUserResponseDto.from(user);
    }

    @Override
    public SysUserResponseDto update(SysUserRequest userDto, HttpServletRequest request) {
        var user = findById(userDto.getId());
        var roles = rolesRepository.findAllById(userDto.getRoleIds());
        if (roles.size() != userDto.getRoleIds().size()) {
            throw new NotFoundException("error.user.not_found.role");
        }

        user.setName(userDto.getName());
        user.setSecondName(userDto.getSecondName());
        user.setTemporaryAccessUntilTime(userDto.getTemporaryAccessUntilTime());
        user.setEditedTime(new Date());
        user.setDeleted(false);
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setBirthdate(userDto.getBirthdate());
        user.setPhoneNumber(userDto.getPhone());
        var result = repository.save(user);
        userDto.setPassword(null);
        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userDto), request);
        return SysUserResponseDto.from(result);
    }

    @Override
    public void delete(Long id, HttpServletRequest request) {
        var user = findById(id);
        user.setDeleted(true);
        repository.save(user);
        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(id),
                request);
    }

    @Override
    public SysUserResponseDto changePassword(SysUserChangePasswordRequestDto userDto, HttpServletRequest request) {
        var result = changePassword(userDto.getEmail(), userDto.getPassword());

        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), String.format("user %d successfully changed the password", result.getId()),
                request);

        return SysUserResponseDto.from(result);
    }

    @Override
    public SysUserResponseDto adminChangePassword(AdminChangePasswordRequestDto changePasswordRequestDto, HttpServletRequest request) {
        var result = changePassword(changePasswordRequestDto.getEmail(), changePasswordRequestDto.getPassword());

        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                String.format("Super admin change password user %d successfully changed the password", result.getId()),
                request);

        return SysUserResponseDto.from(result);

    }

    private SysUser changePassword(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = repository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("error.user.not_found.pin"));

        String currentPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordLastChangeTime(new Date());
        user.setPasswordChangeNextLogon(false);
        user.setEditedTime(new Date());

        if (user.getLastPassword() != null) {
            if (user.getSecondLastPassword() != null) {
                user.setThirdLastPassword(user.getSecondLastPassword());
            }
            user.setSecondLastPassword(user.getLastPassword());
        }

        user.setLastPassword(currentPassword);

        return repository.save(user);
    }

    @Override
    public SysUserResponseDto getByJWT(HttpServletRequest request) {
        var user = baseController.getUserFromToken(request).orElseThrow(() -> new NotFoundException("User not found."));
        return SysUserResponseDto.from(user);
    }

    @Override
    public SysUser ban(Long userId, HttpServletRequest request) {
        var user = findById(userId);
        user.setBanned(true);

        repository.save(user);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userId),
                request);
        return user;
    }

    @Override
    public SysUser unban(Long userId, HttpServletRequest request) {
        var user = findById(userId);

        user.setBanned(false);
        user.setFailedLoginAttempts(0);

        repository.save(user);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userId),
                request);

        return user;
    }

    @Override
    public void updateTheNumberOfFailedLogins(String username) {
        Optional<SysUser> userOptional = repository.findByEmail(username);
        if (userOptional.isPresent()) {
            SysUser user = userOptional.get();
            if (user.getFailedLoginAttempts() == 4) {
                user.setBanned(true);
                repository.save(user);
            } else {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                repository.save(user);
            }

        }

    }

    @Override
    public SysUserResponseDto activateUser(UUID activationCode) {
        var activation = accountActivationRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new NotFoundException("error.activation_code.not_found"));

        if (activation.isActivated()) {
            throw new NotFoundException("error.activation_code.already_activated");
        }

        if (activation.getExpiresAt().before(new Date())) {
            throw new NotFoundException("error.activation_code.is_expires");
        }

        var user = activation.getUser();
        user.setEmailVerified(true);
        repository.save(user);
        activation.setActivated(true);
        activation.setEditedTime(new Date());
        accountActivationRepository.save(activation);
        return SysUserResponseDto.from(user);
    }

    @Override
    public void sendActivationLink(Long userId, HttpServletRequest request) throws MessagingException {
        var user = findById(userId);
        if (user.isEmailVerified()) {
            throw new NotFoundException("error.user.already_activated");
        }
        var allLinks = accountActivationRepository.findAllByUserId(userId);
        accountActivationRepository.deleteAll(allLinks);
        var accountActivation = accountActivationRepository.save(new AccountActivation(user));

     //   mailSender.sendActivationLink(user.getEmail(), accountActivation.getActivationCode());

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userId),
                request);

    }

    public void processForgotPassword(String email) throws MessagingException {
        SysUser user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("error.user.not_found"));

        Date startOfDay = getStartOfDay();
        List<PasswordResetToken> tokensToday = tokenRepository.findByUserAndExpiryDateAfter(user, startOfDay);
        if (tokensToday.size() >= 3) {
            throw new TooManyRequestsException("error.password.reset_limit");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        Integer passwordLength = user.getRoles().stream()
                .map(SysRole::getPasswordLength)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(18);

     //   mailSender.sendPasswordResetLink(user.getEmail(), token, passwordLength);
    }

    public void validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("error.token.not_found"));
        if (resetToken.getExpiryDate().before(new Date())) {
            throw new BadRequestException("error.token.expired_or_invalid");
        }
    }

    public void resetPassword(ResetPasswordRequestDto requestDto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new NotFoundException("error.token.not_found"));

        if (resetToken.getExpiryDate().before(new Date())) {
            throw new BadRequestException("error.token.expired_or_invalid");
        }

        if (requestDto.getNewPassword() == null) {
            throw new BadRequestException("error.password.too_short");
        }

        SysUser user = resetToken.getUser();
        changePassword(user.getEmail(), requestDto.getNewPassword());
        tokenRepository.delete(resetToken);
    }


    @Override
    public SysUser getFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return repository.findByEmail(username).get();
        }
        return null;
    }
}
