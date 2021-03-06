package com.springWorkout.controller;

import com.springWorkout.domain.Person;
import com.springWorkout.responseObject.ConcurrencyResponseObject;
import com.springWorkout.service.ThirdPartyService;
import com.springWorkout.service.ThirdPartyServiceFactory;
import java.security.SecureRandom;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/concurrency")
public class ConcurrenceController {

    private ThirdPartyService thirdPartyService;
    private ThirdPartyServiceFactory thirdPartyServiceFactory;

    @Autowired
    public ConcurrenceController(ThirdPartyService _thirdPartyService,
        ThirdPartyServiceFactory _thirdPartyServiceFactory) {
        thirdPartyService = _thirdPartyService;
        thirdPartyServiceFactory = _thirdPartyServiceFactory;

    }

    @ResponseBody
    public ConcurrencyResponseObject doPost(HttpServletRequest request) {
        String isThirdPartyApiCall = request.getParameter("thirdPartApiCall");
        String requestId = request.getParameter("requestId");
        SecureRandom sr = new SecureRandom();
        String personId = String.valueOf(sr.nextInt());
        Person person = new Person.Builder().id(personId).name("concurrencyName-" + personId)
            .surname("surname" + personId).tckNo("").build();
        ConcurrencyResponseObject concurrencyResponseObject = new ConcurrencyResponseObject();
        thirdPartyService = thirdPartyServiceFactory.getThirdPartyService(isThirdPartyApiCall);
        thirdPartyService.injectPersonDataToResponse(concurrencyResponseObject, person, requestId);
        concurrencyResponseObject.setRequestId(requestId);
        concurrencyResponseObject.setResponseCode("00");
        concurrencyResponseObject.setResponseMessage("Sucessfully");
        return concurrencyResponseObject;
    }
}
