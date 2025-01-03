package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.PremiumCategory;
import net.vrakin.medsalary.repository.PremiumCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Реалізація сервісу для роботи з категоріями премій.
 *
 * <p>Цей клас забезпечує бізнес-логіку для управління об'єктами {@link PremiumCategory}
 * шляхом взаємодії з репозиторієм {@link PremiumCategoryRepository}.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class PremiumCategoryServiceImpl extends AbstractService<PremiumCategory> implements PremiumCategoryService {

    /**
     * Репозиторій для доступу до даних про категорії премій.
     */
    private final PremiumCategoryRepository premiumCategoryRepository;

    /**
     * Конструктор для впровадження залежності репозиторію.
     *
     * @param premiumCategoryRepository Репозиторій для роботи з категоріями премій.
     */
    @Autowired
    public PremiumCategoryServiceImpl(PremiumCategoryRepository premiumCategoryRepository) {
        super(premiumCategoryRepository);
        this.premiumCategoryRepository = premiumCategoryRepository;
    }

    /**
     * Знаходить категорію премії за її назвою.
     *
     * @param name Назва категорії премії.
     * @return {@link Optional}, що містить знайдену категорію, якщо вона існує.
     */
    @Override
    public Optional<PremiumCategory> findByName(String name) {
        return premiumCategoryRepository.findByName(name);
    }

    /**
     * Знаходить категорії премій за їх розміром.
     *
     * @param amount Сума премії.
     * @return Список категорій премій, які відповідають заданій сумі.
     */
    @Override
    public List<PremiumCategory> findByAmount(Integer amount) {
        return premiumCategoryRepository.findByAmount(amount);
    }

    /**
     * Знаходить категорію премії за табельним номером працівника.
     *
     * @param staffListId Табельний номер працівника.
     * @return {@link Optional}, що містить знайдену категорію, якщо вона існує.
     */
    @Override
    public Optional<PremiumCategory> findByStaffListId(String staffListId) {
        return premiumCategoryRepository.findByStaffListId(staffListId);
    }
}
