package net.vrakin.medsalary.exception;

import lombok.*;

import java.time.LocalDate;

/**
 * Клас, що представляє деталі помилки, які повертаються клієнту або записуються у логи.
 * <p>
 * Цей клас надає структурований спосіб передачі інформації про помилку,
 * включаючи час її виникнення, повідомлення про причину, шлях запиту та код помилки.
 * </p>
 *
 * <p>
 * <b>Аннотації:</b>
 * <ul>
 *     <li>{@code @Getter} — генерує геттери для всіх полів.</li>
 *     <li>{@code @Setter} — генерує сеттери для всіх полів.</li>
 *     <li>{@code @NoArgsConstructor} — генерує конструктор без аргументів.</li>
 *     <li>{@code @AllArgsConstructor} — генерує конструктор із параметрами для всіх полів.</li>
 *     <li>{@code @Builder} — забезпечує зручний патерн для побудови об'єктів класу.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {

    /**
     * Час, коли помилка сталася.
     */
    private LocalDate timestamp;

    /**
     * Повідомлення про помилку.
     */
    private String message;

    /**
     * Шлях запиту, який призвів до помилки.
     */
    private String path;

    /**
     * Код помилки для ідентифікації конкретної проблеми.
     */
    private String errorCode;
}
