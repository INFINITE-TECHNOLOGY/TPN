package io.infinite.pigeon.threads

import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.pigeon.conf.OutputQueue
import io.infinite.pigeon.other.MessageStatusSets
import io.infinite.pigeon.springdatarest.OutputMessage
import org.springframework.context.ApplicationContext

@BlackBox
@CompileStatic
class OutputThreadRetry extends OutputThread {

    OutputThreadRetry(OutputQueue outputQueue, InputThread inputThread, ApplicationContext applicationContext) {
        super(outputQueue, inputThread, applicationContext)
        setName(getName() + "_RETRY")
        (1..outputQueue.retryThreadCount).each {
            SenderThread senderThread = new SenderThread(this, it, applicationContext.getEnvironment().getProperty("pigeonOutPluginsDir"))
            senderThreadRobin.add(senderThread)
            senderThread.start()
        }
    }

    @BlackBox(level = CarburetorLevel.ERROR)
    @CompileDynamic
    LinkedHashSet<OutputMessage> masterQuery(String outputQueueName) {
        Date maxLastSendDate
        use(TimeCategory) {
            maxLastSendDate = (new Date() - outputQueue.resendIntervalSeconds.seconds)
        }
        return outputMessageRepository.masterQueryRetry(outputQueueName, MessageStatusSets.OUTPUT_RETRY_MESSAGE_STATUSES.value(), outputQueue.maxRetryCount, maxLastSendDate)
    }

    @Override
    void run() {
        while (true) {
            Set<OutputMessage> outputMessages = masterQuery(outputQueue.getName())
            if (outputMessages.size() > 0) {
                outputMessages.each { outputMessage ->
                    senderEnqueue(outputMessage)
                }
            }
            sleep(outputQueue.pollPeriodMillisecondsRetry)
        }
    }

}
