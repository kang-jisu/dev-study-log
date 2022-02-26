package com.dev.studylog.inheritance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="league_result_v2")
@Getter
@Setter
public class LeagueResultV2 extends LeagueResult{

}
