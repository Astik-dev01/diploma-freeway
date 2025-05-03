package com.example.freeway.util;

import lombok.Getter;

public final class NotificationText {

    private NotificationText() {
    }

    public static final String APPLICATION_APPROVED_OR_NOT = """
        <p>Здравствуйте, <strong>%s %s</strong>!</p>
        <p>Ваша заявка на свободное посещение была <strong>%s</strong> преподавателем <strong>%s %s</strong>.</p>
        <p><strong>Комментарий:</strong> %s</p>
        <p>Вы можете просмотреть статус всех ваших заявок по ссылке:</p>
        <p><a href="http://localhost:5173/approval-list/my" target="_blank">Мои заявки</a></p>
        """;

    public static final String APPLICATION_APPROVED_BY_ALL_TEACHERS = """
        <p>Здравствуйте, <strong>%s %s</strong>!</p>
        <p>Все преподаватели обработали вашу заявку на свободное посещение.</p>
        <p><strong>Результат:</strong> <span style="color:%s">%s</span></p>
        <p>Вы можете просмотреть статус всех своих заявок по ссылке:</p>
        <p><a href="http://localhost:5173/approval-list/my" target="_blank">Мои заявки</a></p>
        """;

    public static final String APPLICATION_CREATED = """
        <p>Ученик <strong>%s %s</strong> оставил заявку на свободное посещение.</p>
        <p><strong>Комментарий:</strong> %s</p>
        <p>Вы можете просмотреть заявку по ссылке:</p>
        <p><a href="http://localhost:5173/approval-list/" target="_blank">Заявки на согласование</a></p>
        """;
}
