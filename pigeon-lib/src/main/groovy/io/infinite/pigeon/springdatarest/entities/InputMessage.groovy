package io.infinite.pigeon.springdatarest.entities

import groovy.transform.ToString
import io.infinite.pigeon.springdatarest.services.PigeonService

import javax.persistence.*

@Entity
@Table(name = "messages")
@ToString(includeNames = true, includeFields = true, excludes = "outputMessages")
class InputMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "tpn_internal_unique_id")
    Long id

    @Column(name = "TXN_ID")
    String externalId

    @Column(name = "SOURCE")
    String sourceName

    @Lob
    @Column(name = "PAYLOAD")
    String payload

    @Column(name = "ENDPOINT")
    String inputQueueName

    @Column(name = "STATUS")
    String status

    @Column(name = "insert_time")
    Date insertTime = new Date()

    @Lob
    String queryParams

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Input2output")
    Set<OutputMessage> outputMessages = new HashSet<>()

    UUID instanceUUID = PigeonService.staticUUID

}
