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
@Table(name = "categories")
@SequenceGenerator(name = "category_id_seq", sequenceName = "category_id_seq", allocationSize = 1, initialValue = 201)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_seq")
    private Long id;

    @NotNull(message = "Category cannot be null.")
    @Column(name = "category")
    private String category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Applicant applicant;
}
