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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "a_languages")
@SequenceGenerator(name = "a_language_id_seq", sequenceName = "a_language_id_seq", allocationSize = 1, initialValue = 201)
public class ApplicantLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "a_language_id_seq")
    private Long id;

    @NotNull(message = "language cannot be null.")
    @Column(name = "language")
    private String language;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Applicant applicant;
}
