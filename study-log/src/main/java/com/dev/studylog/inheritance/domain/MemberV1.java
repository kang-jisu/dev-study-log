package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "accound_id"))
})
public class MemberV1 extends Member {
    private String one;
}