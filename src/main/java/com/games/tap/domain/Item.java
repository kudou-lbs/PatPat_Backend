package com.games.tap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Item {
   @JsonIgnore
   String getId();
}
