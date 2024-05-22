package com.sevenprinciples.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Country {

 @Id
 @GeneratedValue(strategy= GenerationType.AUTO)
 private String id;
 private String name;
 private String countryCode;
 private String capital;
 private double population;
 private String formOfGovernment;

 public Country(String name, String countryCode, String capital, int population, String formOfGovernment) {
  this.name = name;
  this.countryCode = countryCode;
  this.capital = capital;
  this.population = population;
  this.formOfGovernment = formOfGovernment;
 }
}

