package io.infinite.tpn.threads

import groovy.time.TimeCategory
import io.infinite.blackbox.BlackBox
import io.infinite.blackbox.BlackBoxLevel
import io.infinite.tpn.other.MessageStatusSets
import io.infinite.tpn.conf.OutputQueue
import io.infinite.tpn.springdatarest.OutputMessage
import org.springframework.context.ApplicationContext

class OutputThreadRetry extends OutputThread {

    @BlackBox(blackBoxLevel = BlackBoxLevel.EXPRESSION)
    OutputThreadRetry(OutputQueue outputQueue, ApplicationContext applicationContext) {
        super(outputQueue, applicationContext)
        setName(getName() + "-Retry")
        [1..outputQueue.retryThreadCount].each {
            SenderThread senderThread = new SenderThread(outputQueue)
            senderThreadRobin.add(senderThread)
            senderThread.start()
        }
    }

    @BlackBox(blackBoxLevel = BlackBoxLevel.EXPRESSION)
    @Override
    LinkedHashSet<OutputMessage> masterQuery(String outputQueueName) {
        Date maxLastSendDate
        use (TimeCategory) {
            maxLastSendDate = (new Date() - outputQueue.resendIntervalSeconds.seconds)
        }
        return outputMessageRepository.masterQueryRetry(outputQueueName, MessageStatusSets.RETRY_MESSAGE_STATUSES.value(), outputQueue.maxRetryCount, maxLastSendDate)
    }

}
