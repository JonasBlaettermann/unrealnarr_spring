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

 public Country(String name, String countryCode) {
  this.name = name;
  this.countryCode = countryCode;
 }
}

