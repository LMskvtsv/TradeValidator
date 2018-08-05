package ru.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.domain.Trades;
import ru.service.messages.CheckResponse;
import ru.service.validation.ValidationService;

import java.util.Map;

/**
 * Mapping to urls.
 */
@Controller
@ComponentScan(basePackageClasses = ValidationController.class)
public class ValidationController {
    private final ValidationService validationService = new ValidationService();

    /**
     * Transfers request to validation service.
     *
     * @param jsonRequest should be represented as array of trades.
     * @return Response object mapped to internal trade id.
     */
    @PostMapping("/validator")
    @ResponseBody
    public Map<Integer, CheckResponse> validate(@RequestBody Trades jsonRequest) {
        Map<Integer, CheckResponse> response = validationService.validateArray(jsonRequest);
        return response;
    }

    @GetMapping("/validator")
    public String get() {
        return "validator";
    }
}
