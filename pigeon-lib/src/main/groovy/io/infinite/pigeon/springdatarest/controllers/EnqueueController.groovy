package io.infinite.pigeon.springdatarest.controllers

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.pigeon.other.MessageStatuses
import io.infinite.pigeon.springdatarest.entities.InputMessage
import io.infinite.pigeon.springdatarest.repositories.InputMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@Controller
@BlackBox
@Slf4j
class EnqueueController {

    @Value('${pigeonInputPluginsHttpDir}')
    String pigeonInputPluginsHttpDir

    @Autowired
    InputMessageRepository inputMessageRepository

    @PostMapping(value = "/pigeon/enqueue")
    @ResponseBody
    CustomResponse get(HttpServletRequest httpServletRequest) {
        String externalId = httpServletRequest.getParameter("externalId") ?: httpServletRequest.getParameter("txn_id")
        String sourceName = httpServletRequest.getParameter("sourceName") ?: httpServletRequest.getParameter("source")
        String inputQueueName = httpServletRequest.getParameter("inputQueueName") ?: httpServletRequest.getParameter("endpoint")
        InputMessage inputMessage = new InputMessage()
        inputMessage.externalId = externalId
        inputMessage.sourceName = sourceName
        inputMessage.inputQueueName = inputQueueName
        inputMessage.payload = httpServletRequest.getReader().getText()
        inputMessage.status = MessageStatuses.NEW
        inputMessageRepository.save(inputMessage)
        CustomResponse customResponse = new CustomResponse()
        customResponse.response = "Enqueued Successfully"
        return customResponse
    }

}
