package net.vrakin.medsalary.config;

import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.service.SecurityUserService;
import net.vrakin.medsalary.domain.PremiumCategory;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.PremiumCategoryService;
import net.vrakin.medsalary.service.ServicePackageService;
import net.vrakin.medsalary.service.service_package_handler.PremiumKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Сервіс для ініціалізації початкових даних у базі даних.
 *
 * Створює початкових користувачів безпеки, категорії премій та пакети послуг, якщо ці дані відсутні.
 */
@Service
public class InitData {

    private final SecurityUserService securityUserService;
    private final PremiumCategoryService premiumCategoryService;
    private final ServicePackageService servicePackageService;

    /**
     * Конструктор для ініціалізації залежностей.
     *
     * @param securityUserService сервіс для роботи з користувачами безпеки.
     * @param premiumCategoryService сервіс для роботи з категоріями премій.
     * @param servicePackageService сервіс для роботи з пакетами послуг.
     */
    @Autowired
    public InitData(SecurityUserService securityUserService, PremiumCategoryService premiumCategoryService,
                    ServicePackageService servicePackageService) {
        this.securityUserService = securityUserService;
        this.premiumCategoryService = premiumCategoryService;
        this.servicePackageService = servicePackageService;
    }

    /**
     * Метод для ініціалізації даних.
     *
     * Викликає методи створення користувачів, категорій премій та пакетів послуг.
     */
    public void init() {
        createSecurityUsers();
        createPremiumCategory();
        createServicePackage();
    }

    /**
     * Створює категорії премій, якщо вони відсутні у базі даних.
     *
     * Використовує перелік значень з {@link PremiumKind}.
     */
    void createPremiumCategory() {
        if (premiumCategoryService.findAll().isEmpty()) {
            Arrays.stream(PremiumKind.values())
                    .forEach(kind -> {
                        if (premiumCategoryService.findByName(kind.name()).isEmpty())
                            premiumCategoryService.save(
                                    new PremiumCategory(
                                            null,
                                            kind.name(),
                                            kind.getSum(),
                                            1f,
                                            null
                                    )
                            );
                    });
        }
    }

    /**
     * Створює пакети послуг, якщо вони відсутні у базі даних.
     */
    void createServicePackage() {
        if (servicePackageService.findAll().isEmpty()) {
            servicePackageService.save(new ServicePackage(null, "Стаціонарна допомога дорослим та дітям без проведення хірургічних операцій", "4",
                    ServicePackage.HospKind.STATIONARY, ServicePackage.OperationKind.NO_OPERATION, true));
            servicePackageService.save(new ServicePackage(null, "Медична допомога при гострому мозковому інсульті", "5",
                    ServicePackage.HospKind.STATIONARY, ServicePackage.OperationKind.NO_OPERATION, true));
            // ... інші пакети послуг ...
            servicePackageService.save(new ServicePackage(null, "Медичний огляд осіб, який організовується територіальними центрами комплектування та соціальної підтримки", "60",
                    ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true));
        }
    }

    /**
     * Створює користувачів безпеки (адміністратора та звичайного користувача), якщо вони відсутні у базі даних.
     */
    void createSecurityUsers() {
        if (securityUserService.findByLogin("admin").isEmpty()) {
            securityUserService.save(new SecurityUser("admin", "$2a$10$i4NWn8zhFuYVyz9tJPA.6OtV.PFaTIqqW2rgJevq6qHrw9fgWkLei",
                    SecurityRole.ADMIN, "admin@g.com", "+38011-222-33-44", "address", true));
        }

        if (securityUserService.findByLogin("user").isEmpty()) {
            securityUserService.save(new SecurityUser("user", "$2a$10$i4NWn8zhFuYVyz9tJPA.6OtV.PFaTIqqW2rgJevq6qHrw9fgWkLei",
                    SecurityRole.USER, "user@g.com", "+38022-333-44-55", "address", true));
        }
    }
}
