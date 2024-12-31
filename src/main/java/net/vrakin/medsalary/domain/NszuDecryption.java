package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сутність "Розшифровка НСЗУ" (NSZU Decryption).
 *
 * Представляє запис розшифровки даних з НСЗУ, включаючи основну інформацію
 * про пацієнта, діагнози, пакети послуг, статуси звітів і помилок.
 */
@Entity
@Table(name = "nszu_decryption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NszuDecryption implements PeriodControl {

    /**
     * Унікальний ідентифікатор розшифровки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Рік періоду.
     */
    private int yearNum;

    /**
     * Місяць періоду.
     */
    private int monthNum;

    /**
     * Тип запису.
     */
    @Column(name = "record_kind")
    private String recordKind;

    /**
     * Ідентифікатор запису.
     */
    @Column(name = "record_id")
    private String recordID;

    /**
     * Дата створення запису.
     */
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    /**
     * Посада виконавця.
     */
    @Column(name = "executor_user_position")
    private String executorUserPosition;

    /**
     * Ім'я виконавця.
     */
    @Column(name = "executor_name")
    private String executorName;

    /**
     * Місце надання послуг.
     */
    @Column(name = "provider_place")
    private String providerPlace;

    /**
     * Тип направлення.
     */
    @Column(name = "referral_kind")
    private String referralKind;

    /**
     * ЄДРПОУ.
     */
    @Column(name = "edrpou")
    private String edrpou;

    /**
     * Посада направляючого лікаря.
     */
    @Column(name = "referring_user_position")
    private String referringUserPosition;

    /**
     * Ідентифікатор епізоду.
     */
    @Column(name = "episode_id")
    private String episodeId;

    /**
     * Тип епізоду.
     */
    @Column(name = "episode_kind")
    private String episodeKind;

    /**
     * Дата початку епізоду.
     */
    @Column(name = "episode_start_date")
    private LocalDateTime episodeStartDate;

    /**
     * Дата початку періоду.
     */
    @Column(name = "period_start_date")
    private LocalDateTime periodStartDate;

    /**
     * Дата закінчення періоду.
     */
    @Column(name = "period_end_date")
    private LocalDateTime periodEndDate;

    /**
     * Тривалість періоду (в днях).
     */
    @Column(name = "period_lenght")
    private int periodLength;

    /**
     * Основний діагноз.
     */
    @Column(name = "main_diagnosis")
    private String mainDiagnosis;

    /**
     * Статус валідації основного діагнозу.
     */
    @Column(name = "main_diagnosis_validation_status")
    private String mainDiagnosisValidationStatus;

    /**
     * Клінічний статус основного діагнозу.
     */
    @Column(name = "main_diagnosis_clinical_status")
    private String mainDiagnosisClinicalStatus;

    /**
     * Додатковий діагноз.
     */
    @Column(name = "additional_diagnosis")
    private String additionalDiagnosis;

    /**
     * Додатковий діагноз, який спростовано як помилковий.
     */
    @Column(name = "refuted_error_additional_diagnosis")
    private String refutedErrorAdditionalDiagnosis;

    /**
     * Список послуг, наданих пацієнту.
     */
    @Column(name = "service_list")
    private String serviceList;

    /**
     * Клас взаємодії.
     */
    @Column(name = "interaction_class")
    private String interactionClass;

    /**
     * Пріоритет надання послуг.
     */
    @Column(name = "priority")
    private String priority;

    /**
     * Тип взаємодії.
     */
    @Column(name = "interaction_kind")
    private String interactionKind;

    /**
     * Підстави для госпіталізації.
     */
    @Column(name = "grounds_hospitalization")
    private String groundsHospitalization;

    /**
     * Результат лікування.
     */
    @Column(name = "result_treatment")
    private String resultTreatment;

    /**
     * Ідентифікатор пацієнта.
     */
    @Column(name = "patient_id")
    private String patientID;

    /**
     * Наявність декларації.
     */
    @Column(name = "presence_declaration")
    private Boolean presenceDeclaration;

    /**
     * Стать пацієнта.
     */
    @Column(name = "patient_sex")
    private String patientSex;

    /**
     * Вага пацієнта.
     */
    @Column(name = "patient_weight")
    private float patientWeight;

    /**
     * Вік пацієнта у днях.
     */
    @Column(name = "patient_age_days")
    private int patientAgeDays;

    /**
     * Вік пацієнта у роках.
     */
    @Column(name = "patient_age_years")
    private int patientAgeYears;

    /**
     * ADSG-код.
     */
    @Column(name = "adsg")
    private String adsg;

    /**
     * Назва пакету послуг.
     */
    @Column(name = "service_package_name")
    private String servicePackageName;

    /**
     * Номер пакету послуг.
     */
    @Column(name = "service_package_number")
    private String servicePackageNumber;

    /**
     * Тариф у гривнях.
     */
    @Column(name = "tariff_uah")
    private float tariffUAH;

    /**
     * Факт оплати.
     */
    @Column(name = "payment_fact")
    private String paymentFact;

    /**
     * Статус статистики.
     */
    @Column(name = "statistic_status")
    private boolean statisticStatus;

    /**
     * Статус звіту.
     */
    @Column(name = "report_status")
    private boolean reportStatus;

    /**
     * Коментар до помилки.
     */
    @Column(name = "error_comment")
    private String errorComment;

    /**
     * Деталі помилки.
     */
    @Column(name = "error_details")
    private String errorDetails;

    /**
     * Деталі, що потребують перевірки.
     */
    @Column(name = "details_require_verification")
    private String detailsRequireVerification;

    /**
     * Зведені деталі помилки.
     */
    @Column(name = "group_error_details")
    private String groupErrorDetails;

    /**
     * Попередній перегляд даних у НСЗУ.
     */
    @Column(name = "details_preview_nszu")
    private String detailsPreviewNSZU;

    /**
     * Додатковий коментар.
     */
    @Column(name = "additional_comment")
    private String additionalComment;

    /**
     * Дата попереднього перегляду у НСЗУ.
     */
    @Column(name = "date_preview_nszu")
    private LocalDateTime datePreviewNSZU;

    /**
     * Отримує період у вигляді `LocalDate` на основі року та місяця.
     *
     * @return Період у вигляді `LocalDate`.
     */
    @Override
    public LocalDate getPeriod() {
        return LocalDate.of(yearNum, monthNum, 1);
    }

    /**
     * Встановлює період, розділяючи його на рік і місяць.
     *
     * @param period Період у вигляді `LocalDate`.
     */
    @Override
    public void setPeriod(LocalDate period) {
        yearNum = period.getYear();
        monthNum = period.getMonthValue();
    }

    /**
     * Перевизначення методу toString для зручного виводу інформації про розшифровку.
     *
     * @return Рядок із детальною інформацією про розшифровку.
     */
    @Override
    public String toString() {
        return "NszuDecryption{" +
                "id=" + id +
                ", year=" + yearNum +
                ", month=" + monthNum +
                ", recordKind='" + recordKind + '\'' +
                ", recordID='" + recordID + '\'' +
                ", creationDate=" + creationDate +
                ", executorUserPosition='" + executorUserPosition + '\'' +
                ", executorName='" + executorName + '\'' +
                ", providerPlace='" + providerPlace + '\'' +
                ", referralKind='" + referralKind + '\'' +
                ", EDRPOU='" + edrpou + '\'' +
                ", mainDiagnosis='" + mainDiagnosis + '\'' +
                ", servicePackageName='" + servicePackageName + '\'' +
                '}';
    }
}
