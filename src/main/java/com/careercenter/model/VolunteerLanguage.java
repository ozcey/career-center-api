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
@Table(name = "v_languages")
@SequenceGenerator(name = "v_language_id_seq", sequenceName = "v_language_id_seq", allocationSize = 1, initialValue = 801)
public class VolunteerLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "v_language_id_seq")
    private Long id;

    @NotNull(message = "Language cannot be null.")
    @Column(name = "language")
    private String language;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Volunteer volunteer;
}
