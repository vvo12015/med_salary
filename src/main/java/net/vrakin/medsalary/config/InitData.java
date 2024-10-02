package net.vrakin.medsalary.config;

import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.service.SecurityUserService;
import net.vrakin.medsalary.domain.PremiumCategory;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.PremiumCategoryService;
import net.vrakin.medsalary.service.ServicePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitData {

    private final SecurityUserService securityUserService;
    
     private final PremiumCategoryService premiumCategoryService;

    private final ServicePackageService servicePackageService;

    @Autowired
    public InitData(SecurityUserService securityUserService, PremiumCategoryService premiumCategoryService,
            ServicePackageService servicePackageService) {
        this.securityUserService = securityUserService;
        this.premiumCategoryService = premiumCategoryService;
        this.servicePackageService = servicePackageService;
    }

    public void init(){
        createSecurityUsers();
        createPremiumCategory();
        createServicePackage();
    }

    void createPremiumCategory() {
        if (premiumCategoryService.findAll().isEmpty()){
            premiumCategoryService.save(new PremiumCategory(null, "ZERO", 0, 1f, null));
            premiumCategoryService.save(new PremiumCategory(null, "DIAGNOSTIC", 2880, 1f, null));
            premiumCategoryService.save(new PremiumCategory(null, "URGENCY", 2880, 1f, null));
            premiumCategoryService.save(new PremiumCategory(null, "RECEPTION", 0, 1.6f, null));
        }
    }

    void createServicePackage() {
        if (servicePackageService.findAll().isEmpty()){
            servicePackageService.save(new ServicePackage(null, "Стаціонарна допомога дорослим та дітям без проведення хірургічних операцій", "4"
                    , ServicePackage.HospKind.STATIONARY, ServicePackage.OperationKind.NO_OPERATION, true));
            servicePackageService.save(new ServicePackage(null, "Профілактика, діагностика, спостереження та лікування в амбулаторних умовах", "9"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true));
            servicePackageService.save(new ServicePackage(null, "Мамографія", "10"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Гістероскопія", "11"
                    , ServicePackage.HospKind.MIXED, ServicePackage.OperationKind.OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Езофагогастродуоденоскопія", "12"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Колоноскопія", "13"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Цистоскопія", "14"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Бронхоскопія", "15"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Хіміотерапевтичне лікування та супровід дорослих та " +
                    "дітей з онкологічними захворюваннями у амбулаторних та стаціонарних умовах", "17"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Стаціонарна паліативна медична допомога дорослим та дітям", "23"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Ведення вагітності в амбулаторних умовах", "35"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Лікування та супровід дорослих та дітей з гематологічними " +
                    "та онкогематологічними захворюваннями у амбулаторних та стаціонарних умовах\n", "38"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
            servicePackageService.save(new ServicePackage(null, "Хірургічні операції дорослим та дітям в умовах стаціонару одного дня", "47"
                    , ServicePackage.HospKind.AMBULATORY, ServicePackage.OperationKind.NO_OPERATION, true ));
        }
    }

    void createSecurityUsers() {
        if (securityUserService.findByLogin("admin").isEmpty()){
            securityUserService.save(new SecurityUser("admin", "$2a$10$i4NWn8zhFuYVyz9tJPA.6OtV.PFaTIqqW2rgJevq6qHrw9fgWkLei",
                    SecurityRole.ADMIN, "admin@g.com", "+38011-222-33-44", "address", true));
        }

        if (securityUserService.findByLogin("user").isEmpty()){
            securityUserService.save(new SecurityUser("user", "$2a$10$i4NWn8zhFuYVyz9tJPA.6OtV.PFaTIqqW2rgJevq6qHrw9fgWkLei",
                    SecurityRole.USER, "user@g.com", "+38022-333-44-55", "address", true));
        }
    }
}
