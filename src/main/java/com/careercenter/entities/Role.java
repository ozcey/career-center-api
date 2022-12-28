package com.careercenter.entities;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;

import com.careercenter.model.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1, initialValue = 1)
public class Role {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name", length = 60)
    private RoleName name;

}
