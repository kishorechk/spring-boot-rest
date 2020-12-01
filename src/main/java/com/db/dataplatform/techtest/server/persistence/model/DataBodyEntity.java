package com.db.dataplatform.techtest.server.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "DATA_STORE")
@Setter
@Getter
@EqualsAndHashCode
public class DataBodyEntity {

    @Id
    @SequenceGenerator(name = "dataStoreSequenceGenerator", sequenceName = "SEQ_DATA_STORE", allocationSize = 1)
    @GeneratedValue(generator = "dataStoreSequenceGenerator")
    @Column(name = "DATA_STORE_ID")
    @EqualsAndHashCode.Exclude private Long dataStoreId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "DATA_HEADER_ID")
    @EqualsAndHashCode.Exclude private DataHeaderEntity dataHeaderEntity;

    @Column(name = "DATA_BODY")
    private String dataBody;

    @Column(name = "CREATED_TIMESTAMP")
    @EqualsAndHashCode.Exclude private Instant createdTimestamp;

    @PrePersist
    public void setTimestamps() {
        if (createdTimestamp == null) {
            createdTimestamp = Instant.now();
        }
    }
}
