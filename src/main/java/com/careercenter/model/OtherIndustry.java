package com.careercenter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "other_industries")
@SequenceGenerator(name = "other_industries_id_seq", sequenceName = "other_industries_id_seq", allocationSize = 1, initialValue = 501)
public class OtherIndustry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "other_industries_id_seq")
    private Long id;

    @NotNull(message = "Industry cannot be null.")
    @Column(name = "industry")
    private String industry;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Volunteer volunteer;
}
