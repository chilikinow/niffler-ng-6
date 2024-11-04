package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserJson(
    @JsonProperty("username")
    String username) {

}
