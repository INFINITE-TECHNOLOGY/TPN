package io.infinite.tpn.conf

class Subscriber {

    /**
     * Subscriber Name, unique within all Queues.
     */
    String name

    String url

    Integer maxRetryCount = 0

    Integer resendIntervalSeconds = 86400

    Integer normalThreadCount = 4

    Integer retryThreadCount = 0

}