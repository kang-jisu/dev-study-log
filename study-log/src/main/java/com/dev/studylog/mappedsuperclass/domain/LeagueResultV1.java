package com.dev.studylog.mappedsuperclass.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="league_result")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class LeagueResultV1 extends LeagueResult{

}
