package net.vrakin.medsalary.excel;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import net.vrakin.medsalary.mapper.NSZU_DecryptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Slf4j
public class NszuDecryptionExcelReader extends AbstractExcelReader<NszuDecryption, NszuDecryptionDTO>
        implements ExcelReader<NszuDecryption, NszuDecryptionDTO>
{
    @Autowired
    public NszuDecryptionExcelReader(ExcelHelper excelHelper, NSZU_DecryptionMapper mapper) {
        super(excelHelper, mapper);
        this.startColNumber = 5;

        createColumnList();
    }

    @Override
    protected List<String> filterRow(File file) {
        return excelHelper.readExcel(file, startColNumber);
    }

    @Override
    protected void createColumnList() {
        columns.clear();

        columns.add(new Column("Звітний рік", 0));
        columns.add(new Column("Звітний місяць", 1));
        columns.add(new Column("Тип електронного медичного запису (ЕМЗ)", 2));
        columns.add(new Column("ID ЕМЗ", 3));
        columns.add(new Column("Внесено до ЕСОЗ", 4));
        columns.add(new Column("Посада медичного працівника (виконавця) з ЕМЗ", 5));
        columns.add(new Column("Медичний працівник (виконавець) з ЕМЗ", 6));
        columns.add(new Column("Місце надання послуг зазначене в ЕМЗ", 7));
        columns.add(new Column("Тип направлення", 8));
        columns.add(new Column("Код ЄДРПОУ закладу де видано напрвлення", 9));
        columns.add(new Column("Посада лікаря який видав направлення", 10));
        columns.add(new Column("ID епізоду", 11));
        columns.add(new Column("Тип епізоду", 12));
        columns.add(new Column("Дата та час початку епізоду / початку госпіталізації", 13));
        columns.add(new Column("Дата та час початку періоду/ забору (ДЗ)", 14));
        columns.add(new Column("Дата та час кінця періоду/ виписки зі стаціонару", 15));
        columns.add(new Column("Тривалість лікування, в днях", 16));
        columns.add(new Column("Основний діагноз", 17));
        columns.add(new Column("Статус достовірності основного діагнозу", 18));
        columns.add(new Column("Клінічний статус основного діагнозу", 19));
        columns.add(new Column("Додаткові/супутні діагнози", 20));
        columns.add(new Column("Спростовані та помилкові додаткові діагнози", 21));
        columns.add(new Column("Перелік інтервенцій (послуги/діагностичні звіти/процедури)", 22));
        columns.add(new Column("Клас взаємодії", 23));
        columns.add(new Column("Приорітет", 24));
        columns.add(new Column("Тип взаємодії", 25));
        columns.add(new Column("Підстава звернення в стаціонар (госпіталізації)", 26));
        columns.add(new Column("Результат лікування (виписки)", 27));
        columns.add(new Column("Унікальний код пацієнта", 28));
        columns.add(new Column("Наявність декларації в пацієнта на момент створення ЕМЗ", 29));
        columns.add(new Column("Стать пацієнта", 30));
        columns.add(new Column("Вага при госпіталізації, грам", 31));
        columns.add(new Column("Вік пацієнта в днях на момент створення ЕМЗ", 32));
        columns.add(new Column("Вік пацієнта в роках на момент створення ЕМЗ", 33));
        columns.add(new Column("АДСГ", 34));
        columns.add(new Column("Пакет послуг", 35));
        columns.add(new Column("Номер послуги", 36));
        columns.add(new Column("Тариф, грн", 37));
        columns.add(new Column("Фактична частина оплати за період (без ГБ), грн", 38));
         columns.add(new Column("Включення медичного запису до статистики", 39));
        columns.add(new Column("Включення медичного запису до звіту", 40));
        columns.add(new Column("Коментар щодо виявлених помилок", 41));
        columns.add(new Column("Деталі виявлених помилок за результатами автоматичної перевірки ЕМЗ щодо відповідності критеріям повноти та достовірності даних", 42));
        columns.add(new Column("Деталі виявлених невідповідностей, які потребують додаткової перевірки та верифікації з боку надавача послуг", 43));
        columns.add(new Column("Деталі помилок/конфліктів при групуванні", 44));
        columns.add(new Column("Деталі перегляду НСЗУ пакету медичних послуг", 45));
        columns.add(new Column("Додаткові коментарі/зауваження за результатами автоматичних перевірок", 46));
        columns.add(new Column("Дата перегляду НСЗУ пакету медичних послуг", 47));
    }

    @Override
    public NszuDecryptionDTO toDTOFromString(String stringDTO) {

            List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                    .collect(Collectors.toList());

            stringList = stringList.stream().map(s->{
                if (s.equals(ExcelHelper.EMPTY_SING_EXCEL)) {
                    return ExcelHelper.EMPTY_SING;
                }
                return s;
            }).toList();

            NszuDecryptionDTO dto = new NszuDecryptionDTO();
            int index = 0;
                dto.setYear(Integer.parseInt(stringList.get(index++)));
                dto.setMonth(Integer.parseInt(stringList.get(index++)));
                dto.setRecordKind(truncateString(stringList.get(index++)));
                dto.setRecordID(truncateString(stringList.get(index++)));
                dto.setCreationDate(excelHelper.mapToDate(stringList.get(index++)));
                dto.setExecutorUserPosition(truncateString(stringList.get(index++)));
                dto.setExecutorName(truncateString(stringList.get(index++)));
                dto.setProviderPlace(truncateString(stringList.get(index++)));
                dto.setReferralKind(truncateString(stringList.get(index++)));
                dto.setEdrpou(truncateString(stringList.get(index++)));
                dto.setReferringUserPosition(truncateString(stringList.get(index++)));
                dto.setEpisodeId(truncateString(stringList.get(index++)));
                dto.setEpisodeKind(truncateString(stringList.get(index++)));
                dto.setEpisodeStartDate(excelHelper.mapToDate(stringList.get(index++)));
                dto.setPeriodStartDate(excelHelper.mapToDate(stringList.get(index++)));
                dto.setPeriodEndDate(excelHelper.mapToDate(stringList.get(index++)));
                dto.setPeriodLength(Integer.parseInt(stringList.get(index++)));
                dto.setMainDiagnosis(truncateString(stringList.get(index++)));
                dto.setMainDiagnosisValidationStatus(truncateString(stringList.get(index++)));
                dto.setMainDiagnosisClinicalStatus(truncateString(stringList.get(index++)));
                dto.setAdditionalDiagnosis(truncateString(stringList.get(index++)));
                dto.setRefutedErrorAdditionalDiagnosis(truncateString(stringList.get(index++)));
                dto.setServiceList(truncateString(stringList.get(index++)));
                dto.setInteractionClass(truncateString(stringList.get(index++)));
                dto.setPriority(truncateString(stringList.get(index++)));
                dto.setInteractionKind(truncateString(stringList.get(index++)));
                dto.setGroundsHospitalization(truncateString(stringList.get(index++)));
                dto.setResultTreatment(truncateString(stringList.get(index++)));
                dto.setPatientID(truncateString(stringList.get(index++)));
                dto.setPresenceDeclaration(Boolean.parseBoolean(stringList.get(index++)));
                dto.setPatientSex(truncateString(stringList.get(index++)));
                dto.setPatientWeight(Float.parseFloat(stringList.get(index++)));
                dto.setPatientAgeDays(Integer.parseInt(stringList.get(index++)));
                dto.setPatientAgeYears(Integer.parseInt(stringList.get(index++)));
                dto.setAdsg(truncateString(stringList.get(index++)));
                dto.setServicePackageName(truncateString(stringList.get(index++)));
                dto.setServicePackageNumber(truncateString(stringList.get(index++)));
                dto.setTariffUAH(Float.parseFloat(stringList.get(index++)));
                dto.setPaymentFact(truncateString(stringList.get(index++)));
                dto.setStatisticStatus(Boolean.parseBoolean(stringList.get(index++)));
                dto.setReportStatus(Boolean.parseBoolean(stringList.get(index++)));
                dto.setErrorComment(truncateString(stringList.get(index++)));
                dto.setErrorDetails(truncateString(stringList.get(index++)));
                dto.setDetailsRequireVerification(truncateString(stringList.get(index++)));
                dto.setGroupErrorDetails(truncateString(stringList.get(index++)));
                dto.setDetailsPreviewNSZU(truncateString(stringList.get(index++)));
                dto.setAdditionalComment(truncateString(stringList.get(index++)));
                dto.setDatePreviewNSZU(excelHelper.mapToDate(stringList.get(index++)));
            return dto;
    }




    private String truncateString(String input) {
        if (input.length() > 255) {
            return input.substring(0, 250);
        }
        return input;
    }
}
