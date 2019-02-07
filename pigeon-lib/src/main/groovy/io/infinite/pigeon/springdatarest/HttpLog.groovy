package io.infinite.pigeon.springdatarest

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
@ToString(includeNames = true, includeFields = true)
@CompileStatic
class HttpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    Date requestDate
    @Lob
    String requestHeaders
    @Lob
    String requestBody
    String method
    String url
    String requestStatus
    @Lob
    String requestExceptionString
    Date responseDate
    @Lob
    String responseHeaders
    @Lob
    String responseBody
    Integer responseStatus
    Date insertDate = new Date()
    @ManyToOne(fetch = FetchType.EAGER)
    OutputMessage outputMessage

}
