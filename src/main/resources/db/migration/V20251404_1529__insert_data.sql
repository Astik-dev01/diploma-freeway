INSERT INTO sys_roles (alias, name_ru, name_ky, password_length)
VALUES ('SUPER_ADMIN', 'Супер Админ', 'Супер Админ', 12),
       ('STUDENT', 'Студент', 'Студент', 12),
       ('TEACHER', 'Учитель', 'Мугалим', 12),
       ('ADVISOR', 'Куратор', 'Куратор', 12);

INSERT INTO sys_users (second_name, name, gender, password, email, email_verified, phone_number_verified,
                       deleted, created_time, password_change_next_logon, is_banned, phone_number)
VALUES
-- SUPER_ADMIN
('Админов', 'Админ', 'MALE',
 '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6', -- password: "admin"
 'admin@test.com', true, true, false, CURRENT_TIMESTAMP, false, false, '+996700000001'),

-- STUDENT
('Студентов', 'Студент', 'MALE',
 '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6', -- password: "admin"
 'student@test.com', true, true, false, CURRENT_TIMESTAMP, false, false, '+996700000002'),

-- TEACHER
('Учителько', 'Анна', 'FEMALE',
 '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6', -- password: "admin"
 'teacher@test.com', true, true, false, CURRENT_TIMESTAMP, false, false, '+996700000003'),

-- ADVISOR
('Кеңешов', 'Марат', 'MALE',
 '$2a$10$DaMPIsKYoU/6uPnignPxN.xl8Lkmd7INBcduIYGArK/82nLotVcO6', -- password: "admin"
 'advisor@test.com', true, true, false, CURRENT_TIMESTAMP, false, false, '+996700000004');



-- допустим ID ролей: 1=SUPER_ADMIN, 2=STUDENT, 3=TEACHER, 4=ADVISOR
-- и ID пользователей: 1=admin, 2=student, 3=teacher, 4=advisor

INSERT INTO sys_user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);
SELECT setval(pg_get_serial_sequence('sys_roles', 'id'), (SELECT MAX(id) FROM sys_roles));
SELECT setval(pg_get_serial_sequence('sys_users', 'id'), (SELECT MAX(id) FROM sys_users));


INSERT INTO error_message (id, field, message_ru, message_kg, message_eng)
VALUES ('error.not_found', 'id', 'Сущность с таким идентификатором не найдена', 'Бул ID менен объект табылган жок',
        'Entity with this ID not found'),
       ('error.validation.default', 'default', 'Ошибка валидации', 'Текшерүү катасы', 'Validation error'),
       ('error.auth', 'username', 'Неправильный логин или пароль', 'Туура эмес логин же сырсөз',
        'Invalid login or password'),
       ('error.access.timed_out', 'username', 'Время доступа истекло', 'Кирүү убактысы бүттү', 'Access timed out'),
       ('error.role.not_found', 'id', 'Роль с таким идентификатором не найдена', 'Бул идентификатор менен рол жок',
        'Role with this identifier not found'),
       ('error.user.not_found', 'id', 'Пользователь с таким идентификатором не найден',
        'Бул идентификатор менен колдонуучу табылган жок', 'User with this identifier not found'),
       ('error.alias.exists', 'alias', 'Псевдоним уже существует', 'Лакап ат мурунтан эле бар', 'Alias already exists'),
       ('error.user.not_found.role', 'role', 'Некоторые роли не найдены', 'Кээ бир ролдор табылган жок',
        'Some roles were not found'),
       ('error.valid.email.format', 'email', 'Email не соответствует формату', 'Email форматына туура келбейт',
        'Email does not match the format'),
       ('error.valid.email.not_null', 'email', 'Почта не должна быть пустой', 'Почта бош болбошу керек',
        'Email cannot be null'),
       ('error.user.not_found.email', 'email', 'Пользователь с такой почтой не найден',
        'Колдонуучу мындай почта менен табылган жок', 'The user with such email was not found'),
       ('error.valid.user.email', 'email', 'Указанная почта уже зарегистрирована',
        'Көрсөтүлгөн почта буга чейин катталган', 'The specified mail has already been registered'),

       ('error.valid.password', 'password',
        'Пароль должен соответствовать требованиям, включая заглавную букву, цифру и специальный символ и иметь длину 18',
        'Сырсөз талаптарга жооп бериши керек, анын ичинде баш тамга, сан жана атайын белги болушу жана узундугу 18 символ болушу керек',
        'The password must meet the requirements, including an uppercase letter, a number, a special character, and be 18 characters long'),


       ('error.valid.password_change.not_own', 'pin', 'Вы не можете изменить пароль другого пользователя',
        'Сиз башка колдонуучунун сырсөзүн өзгөртө албайсыз', 'You cannot change another users password'),
       ('error.valid.old_password', 'oldPassword', 'Старый пароль не должен быть пустым',
        'Эски сырсөз бош болбошу керек', 'Old password cannot be empty'),
       ('error.valid.old_password.incorrect', 'oldPassword', 'Вы ввели неверный старый пароль',
        'Сиз эски паролду туура эмес киргиздиңиз',
        'You entered an incorrect old password'),

       ('error.access.not_activated', 'username', 'Аккаунт еще не активирован',
        'Аккаунт дагы эле активдештирилген эмес', 'Account is not yet activated'),
       ('error.activation_code.is_expires', 'activation_code',
        'Срок ссылки истек, запросите новую ссылку у администратора',
        'Шилтеменин мөөнөтү бүттү, администратордон жаңы шилтеме сураныңыз',
        'The link has expired, please request a new one from the administrator'),
       ('error.activation_code.already_activated', 'activation_code', 'Аккаунт уже активирован',
        'Аккаунт мурунтан эле активдештирилген',
        'The account is already activated'),
       ('error.user.already_activated', 'userId', 'Этот пользователь уже активирован',
        'Бул колдонуучу мурунтан эле активдештирилген',
        'This user is already activated'),
       ('error.valid.alias.not_null', 'alias', 'Уникальное наименование не должно быть пустым',
        'Уникалдуу аты бош болбошу керек', 'The unique name should not be empty'),
       ('error.valid.alias.not_unique', 'alias', 'Уникальное наименование должно быть уникальным',
        'Уникалдуу аты кайталанбашы керек', 'The unique name must be unique'),


       ('error.valid.phoneNumber.is_blank', 'phoneNumber', 'Номер телефона не должен быть пустым.',
        'Телефон номери бош болбошу керек.', 'Phone number must not be blank.')
        ,

       ('error.phone.invalid_format', 'phoneNumber', 'Номер телефона должен быть в формате +996xxxxxxxxx.',
        'Телефон номери +996xxxxxxxxx форматында болушу керек.', 'Phone number must be in the format +996xxxxxxxxx.')
        ,
       ('error.phone.invalid_number', 'phoneNumber',
        'Номер телефона недействителен.',
        'Телефон номери жараксыз.',
        'Invalid phone number.'),

       ('error.access.denied', 'username', 'Доступ закрыт', 'Кирүү жабык', 'Access denied'),

       ('error.token.expired_or_invalid', 'username', 'Срок действия токена истек или он недействителен',
        'Токендин мөөнөтү бүттү же жараксыз', 'Token expired or invalid'),

       ('error.mail.send_failed', 'email',
        'Не удалось отправить письмо на указанный адрес',
        'Көрсетүлгөн дарекке кат жөнөтүү мүмкүн болгон жок', 'Failed to send email to the specified address'),

       ('error.valid.user.phone.exists',
        'phone',
        'Пользователь с таким номером телефона уже существует',
        'Мындай телефон номери бар колдонуучу мурунтан эле бар',
        'A user with this phone number already exists'),

       ('error.service_photo.not_found', 'serviceId',
        'Фото для данной услуги не найдено',
        'Бул кызмат үчүн сүрөт табылган жок',
        'Photo for this service not found'),

       ('error.password.too_short', 'password', 'Пароль должен содержать минимум %d символов',
        'Купуя сөз кеминде %d белгиден турушу керек', 'Password must be at least %d characters long'),
       ('error.password.reset_limit', 'email', 'Лимит сброса пароля исчерпан (3 раза в день)',
        'Купуя сөздү калыбына келтирүүнүн чеги аяктады (күнүнө 3 жолу)',
        'Password reset limit exceeded (3 times per day)'),
       ('error.phone.service.invalid_format',
        'phone',
        'Номер телефона {0} недействителен или не соответствует формату страны.',
        'Телефон номери {0} туура эмес же өлкөнүн форматына туура келбейт.',
        'Phone number {0} is invalid or does not match the expected country format.'),

       ('error.valid.country_code.not_blank', 'countryCode',
        'Код страны не может быть пустым', 'Өлкөнүн коду бош болбошу керек',
        'Country code must not be blank'),

       ('error.valid.address.not_blank', 'address',
        'Адрес не может быть пустым', 'Дарек бош болбошу керек',
        'Address must not be blank'),
       ('error.create_directory', 'file', 'Не удалось создать директорию для сохранения файла',
        'Файл сактоочу каталог түзүлгөн жок', 'Failed to create directory for file storage'),

       ('error.file_not_found', 'file', 'Файл не найден', 'Файл табылган жок', 'File not found'),

       ('error.file_body.unacceptable.format', 'file',
        'Недопустимый формат файла. Разрешены только PNG, JPG, JPEG, PDF',
        'Файлдын форматы туура эмес. Болгону PNG, JPG, JPEG, PDF форматтарына уруксат берилет',
        'Unsupported file format. Only PNG, JPG, JPEG, and PDF are allowed'),

       ('error.file_save_failed', 'file', 'Не удалось сохранить файл', 'Файл сакталбай калды', 'Failed to save file');


INSERT INTO faculties (name, deleted)
VALUES
    ('Факультет информационных технологий', false),
    ('Факультет экономики и управления', false),
    ('Факультет юриспруденции', false),
    ('Факультет филологии и журналистики', false),
    ('Факультет медицины', false),
    ('Факультет архитектуры и строительства', false),
    ('Факультет международных отношений', false),
    ('Факультет педагогики и психологии', false),
    ('Факультет математики и физики', false),
    ('Факультет агрономии и экологии', false);
