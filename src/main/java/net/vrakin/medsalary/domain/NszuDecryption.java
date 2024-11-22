package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nszu_decryption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NszuDecryption implements PeriodControl{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int yearNum;

    private int monthNum;

    @Column(name = "record_kind")
    private String recordKind;

    @Column(name = "record_id")
    private String recordID;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "executor_user_position")
    private String executorUserPosition;

    @Column(name = "executor_name")
    private String executorName;

    @Column(name = "provider_place")
    private String providerPlace;

    @Column(name = "referral_kind")
    private String referralKind;

    @Column(name = "edrpou")
    private String edrpou;

    @Column(name = "referring_user_position")
    private String referringUserPosition;

    @Column(name = "episode_id")
    private String episodeId;

    @Column(name = "episode_kind")
    private String episodeKind;

    @Column(name = "episode_start_date")
    private LocalDateTime episodeStartDate;

    @Column(name = "period_start_date")
    private LocalDateTime periodStartDate;

    @Column(name = "period_end_date")
    private LocalDateTime periodEndDate;

    @Column(name = "period_lenght")
    private int periodLength;

    @Column(name = "main_diagnosis")
    private String mainDiagnosis;

    @Column(name = "main_diagnosis_validation_status")
    private String mainDiagnosisValidationStatus;

    @Column(name = "main_diagnosis_clinical_status")
    private String mainDiagnosisClinicalStatus;

    @Column(name = "additional_diagnosis")
    private String additionalDiagnosis;

    @Column(name = "refuted_error_additional_diagnosis")
    private String refutedErrorAdditionalDiagnosis;

    @Column(name = "service_list")
    private String serviceList;

    @Column(name = "interaction_class")
    private String interactionClass;

    @Column(name = "priority")
    private String priority;

    @Column(name = "interaction_kind")
    private String interactionKind;

    @Column(name = "grounds_hospitalization")
    private String groundsHospitalization;

    @Column(name = "result_treatment")
    private String resultTreatment;

    @Column(name = "patient_id")
    private String patientID;

    @Column(name = "presence_declaration")
    private Boolean presenceDeclaration;

    @Column(name = "patient_sex")
    private String patientSex;

    @Column(name = "patient_weight")
    private float patientWeight;

    @Column(name = "patient_age_days")
    private int patientAgeDays;

    @Column(name = "patient_age_years")
    private int patientAgeYears;

    @Column(name = "adsg")
    private String adsg;

    @Column(name = "service_package_name")
    private String servicePackageName;

    @Column(name = "service_package_number")
    private String servicePackageNumber;

    @Column(name = "tariff_uah")
    private float tariffUAH;

    @Column(name = "payment_fact")
    private String paymentFact;

    @Column(name = "statistic_status")
    private boolean statisticStatus;

    @Column(name = "report_status")
    private boolean reportStatus;

    @Column(name = "error_comment")
    private String errorComment;

    @Column(name = "error_details")
    private String errorDetails;

    @Column(name = "details_require_verification")
    private String detailsRequireVerification;

    @Column(name = "group_error_details")
    private String groupErrorDetails;

    @Column(name = "details_preview_nszu")
    private String detailsPreviewNSZU;

    @Column(name = "additional_comment")
    private String additionalComment;

    @Column(name = "date_preview_nszu")
    private LocalDateTime datePreviewNSZU;

    @Override
    public LocalDate getPeriod() {
        return LocalDate.of(yearNum, monthNum, 1);
    }
    @Override
    public void setPeriod(LocalDate period) {
        yearNum = period.getYear();
        monthNum = period.getMonthValue();
    }

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
                ", referringUserPosition='" + referringUserPosition + '\'' +
                ", episodeId='" + episodeId + '\'' +
                ", episodeKind='" + episodeKind + '\'' +
                ", episodeStartDate=" + episodeStartDate +
                ", periodStartDate=" + periodStartDate +
                ", periodEndDate=" + periodEndDate +
                ", periodLength=" + periodLength +
                ", mainDiagnosis='" + mainDiagnosis + '\'' +
                ", mainDiagnosisValidationStatus='" + mainDiagnosisValidationStatus + '\'' +
                ", mainDiagnosisClinicalStatus='" + mainDiagnosisClinicalStatus + '\'' +
                ", additionalDiagnosis='" + additionalDiagnosis + '\'' +
                ", refutedErrorAdditionalDiagnosis='" + refutedErrorAdditionalDiagnosis + '\'' +
                ", serviceList='" + serviceList + '\'' +
                ", interactionClass='" + interactionClass + '\'' +
                ", priority='" + priority + '\'' +
                ", interactionKind='" + interactionKind + '\'' +
                ", groundsHospitalization='" + groundsHospitalization + '\'' +
                ", resultTreatment='" + resultTreatment + '\'' +
                ", patientID='" + patientID + '\'' +
                ", presenceDeclaration=" + presenceDeclaration +
                ", patientSex='" + patientSex + '\'' +
                ", patientWeight=" + patientWeight +
                ", patientAgeDays=" + patientAgeDays +
                ", patientAgeYears=" + patientAgeYears +
                ", ADSG='" + adsg + '\'' +
                ", servicePackageName='" + servicePackageName + '\'' +
                ", servicePackageNumber='" + servicePackageNumber + '\'' +
                ", tariffUAH=" + tariffUAH +
                ", paymentFact='" + paymentFact + '\'' +
                ", statisticStatus=" + statisticStatus +
                ", reportStatus=" + reportStatus +
                ", errorComment='" + errorComment + '\'' +
                ", errorDetails='" + errorDetails + '\'' +
                ", detailsRequireVerification='" + detailsRequireVerification + '\'' +
                ", groupErrorDetails='" + groupErrorDetails + '\'' +
                ", detailsPreviewNSZU='" + detailsPreviewNSZU + '\'' +
                ", additionalComment='" + additionalComment + '\'' +
                ", datePreviewNSZU=" + datePreviewNSZU +
                '}';
    }
}
