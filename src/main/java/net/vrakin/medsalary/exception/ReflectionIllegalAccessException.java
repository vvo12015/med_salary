package net.vrakin.medsalary.exception;

import lombok.Getter;

/**
 * Виключення, яке вказує на помилку доступу до ресурсу через рефлексію.
 *
 * <p>
 * Це виключення використовується, коли доступ до методу або поля через рефлексію
 * є недозволеним, наприклад, через обмеження видимості або безпеки.
 * </p>
 *
 * <p>
 * <b>Основні можливості:</b>
 * <ul>
 *     <li>Зберігає назву ресурсу, для якого виникла помилка доступу.</li>
 *     <li>Розширює стандартне {@code IllegalAccessException}, додаючи більш детальну інформацію.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@Getter
public class ReflectionIllegalAccessException extends IllegalAccessException {

    /**
     * Назва ресурсу, до якого не вдалося отримати доступ через рефлексію.
     */
    private final String resourceName;

    /**
     * Конструктор виключення {@code ReflectionIllegalAccessException}.
     *
     * @param resourceName Назва ресурсу, доступ до якого викликав помилку.
     */
    public ReflectionIllegalAccessException(String resourceName) {
        super(String.format("Illegal access attempt to resource: %s", resourceName));
        this.resourceName = resourceName;
    }
}
