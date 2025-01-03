package net.vrakin.medsalary.dto;

import lombok.*;
import net.vrakin.medsalary.domain.PeriodControl;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для сутності NszuDecryption.
 *
 * Використовується для передачі даних про декодування НСЗУ (Національної служби здоров'я України)
 * між різними рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NszuDecryptionDTO implements PeriodControl {

    /**
     * Унікальний ідентифікатор запису.
     */
    private Long id;

    /**
     * Рік періоду запису.
     */
    private int year;

    /**
     * Місяць періоду запису.
     */
    private int month;

    /**
     * Тип запису.
     */
    private String recordKind;

    /**
     * Унікальний ідентифікатор запису НСЗУ.
     */
    private String recordID;

    /**
     * Дата створення запису.
     */
    private LocalDateTime creationDate;

    /**
     * Посада виконавця.
     */
    private String executorUserPosition;

    /**
     * Ім'я виконавця.
     */
    private String executorName;

    /**
     * Місце надання послуг.
     */
    private String providerPlace;

    /**
     * Тип направлення.
     */
    private String referralKind;

    /**
     * ЄДРПОУ закладу.
     */
    private String edrpou;

    /**
     * Посада особи, що направила пацієнта.
     */
    private String referringUserPosition;

    /**
     * Ідентифікатор епізоду.
     */
    private String episodeId;

    /**
     * Тип епізоду.
     */
    private String episodeKind;

    /**
     * Дата початку епізоду.
     */
    private LocalDateTime episodeStartDate;

    /**
     * Дата початку періоду.
     */
    private LocalDateTime periodStartDate;

    /**
     * Дата закінчення періоду.
     */
    private LocalDateTime periodEndDate;

    /**
     * Тривалість періоду (в днях).
     */
    private int periodLength;

    /**
     * Основний діагноз.
     */
    private String mainDiagnosis;

    /**
     * Статус валідації основного діагнозу.
     */
    private String mainDiagnosisValidationStatus;

    /**
     * Клінічний статус основного діагнозу.
     */
    private String mainDiagnosisClinicalStatus;

    /**
     * Додатковий діагноз.
     */
    private String additionalDiagnosis;

    /**
     * Відхилені або помилкові додаткові діагнози.
     */
    private String refutedErrorAdditionalDiagnosis;

    /**
     * Список послуг.
     */
    private String serviceList;

    /**
     * Клас взаємодії.
     */
    private String interactionClass;

    /**
     * Пріоритет.
     */
    private String priority;

    /**
     * Тип взаємодії.
     */
    private String interactionKind;

    /**
     * Підстави госпіталізації.
     */
    private String groundsHospitalization;

    /**
     * Результат лікування.
     */
    private String resultTreatment;

    /**
     * Ідентифікатор пацієнта.
     */
    private String patientID;

    /**
     * Наявність декларації.
     */
    private Boolean presenceDeclaration;

    /**
     * Стать пацієнта.
     */
    private String patientSex;

    /**
     * Вага пацієнта (кг).
     */
    private float patientWeight;

    /**
     * Вік пацієнта (в днях).
     */
    private int patientAgeDays;

    /**
     * Вік пацієнта (в роках).
     */
    private int patientAgeYears;

    /**
     * Інформація про АДСГ.
     */
    private String adsg;

    /**
     * Назва пакету послуг.
     */
    private String servicePackageName;

    /**
     * Номер пакету послуг.
     */
    private String servicePackageNumber;

    /**
     * Тариф у гривнях (UAH).
     */
    private float tariffUAH;

    /**
     * Факт оплати.
     */
    private String paymentFact;

    /**
     * Статус у статистиці.
     */
    private boolean statisticStatus;

    /**
     * Статус звітності.
     */
    private boolean reportStatus;

    /**
     * Коментар щодо помилок.
     */
    private String errorComment;

    /**
     * Деталі помилок.
     */
    private String errorDetails;

    /**
     * Інформація про деталі, що вимагають перевірки.
     */
    private String detailsRequireVerification;

    /**
     * Група деталей помилок.
     */
    private String groupErrorDetails;

    /**
     * Деталі попереднього перегляду НСЗУ.
     */
    private String detailsPreviewNSZU;

    /**
     * Додатковий коментар.
     */
    private String additionalComment;

    /**
     * Дата попереднього перегляду НСЗУ.
     */
    private LocalDateTime datePreviewNSZU;

    /**
     * Отримує період у вигляді LocalDate.
     *
     * @return перший день місяця відповідного року і місяця.
     */
    @Override
    public LocalDate getPeriod() {
        return LocalDate.of(year, month, 1);
    }

    /**
     * Встановлює період у вигляді LocalDate.
     *
     * @param period дата, з якої встановлюється рік і місяць.
     */
    @Override
    public void setPeriod(LocalDate period) {
        year = period.getYear();
        month = period.getMonthValue();
    }
}
