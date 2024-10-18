package net.vrakin.medsalary.controller;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.excel.entity.reader.ResultExcelWriter;
import net.vrakin.medsalary.generator.GeneratorResultService;
import net.vrakin.medsalary.service.StaffListRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/result")
@Slf4j
public class ResultController {

    public static final int THREADS_COUNT = 10;
    private final GeneratorResultService generatorResultService;

    private final StaffListRecordService staffListRecordService;
    private final ResultExcelWriter resultExcelWriter;

    public ResultController(GeneratorResultService generatorResultService,
                            StaffListRecordService staffListRecordService,
                            ResultExcelWriter resultExcelWriter) {
        this.generatorResultService = generatorResultService;
        this.staffListRecordService = staffListRecordService;
        this.resultExcelWriter = resultExcelWriter;
    }

    private List<Result> resultList = new ArrayList<>();

    @GetMapping
    public String result(Model model){
        log.info("Accessing result page");

        model.addAttribute("resultList", resultList);

        return "result";
    }

    @GetMapping("/generate")
    public String resultGenerate(Model model) throws IOException {
        log.info("Accessing generate result page");

        List<StaffListRecord> staffListRecordList = staffListRecordService.findAll();

        resultList = staffListRecordList.stream().map(s -> {
            try {
                return generatorResultService.generate(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })
        .toList();

        resultExcelWriter.writeAll(resultList);
        model.addAttribute("result_count", resultList.size());
        model.addAttribute("results", resultList);

        return "result";
    }


}
