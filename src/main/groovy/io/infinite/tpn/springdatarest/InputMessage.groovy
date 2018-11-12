package io.infinite.tpn.springdatarest

import javax.persistence.*

@Entity
@Table(name = "messages")
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Input2output")
    Set<OutputMessage> outputMessages = new HashSet<>()

}
