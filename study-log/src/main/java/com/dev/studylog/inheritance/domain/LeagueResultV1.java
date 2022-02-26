package com.dev.studylog.inheritance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="league_result")
@Getter
@Setter
public class LeagueResultV1 extends LeagueResult{

}
