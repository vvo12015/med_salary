package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.exception.CalculateTypeNotFoundException;
import net.vrakin.medsalary.generator.GeneratorResultService;
import net.vrakin.medsalary.mapper.StaffListRecordMapper;
import net.vrakin.medsalary.service.StaffListRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/result")
@Slf4j
public class ResultController {

    private GeneratorResultService generatorResultService;

    private StaffListRecordService staffListRecordService;

    private StaffListRecordMapper staffListRecordMapper;

    public ResultController(GeneratorResultService generatorResultService, StaffListRecordService staffListRecordService,
                            StaffListRecordMapper staffListRecordMapper) {
        this.generatorResultService = generatorResultService;
        this.staffListRecordService = staffListRecordService;
        this.staffListRecordMapper = staffListRecordMapper;
    }

    @GetMapping
    public String result(Model model){
        log.info("Accessing result page");

        List<StaffListRecord> staffListRecordList = staffListRecordService.findAll();


        model.addAttribute("results", staffListRecordList.stream().map(s-> {
                    try {
                        return generatorResultService.generateResult(s);
                    } catch (CalculateTypeNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()));
        return "result";
    }

}
