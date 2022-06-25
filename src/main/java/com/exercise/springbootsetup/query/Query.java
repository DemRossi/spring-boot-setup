package com.exercise.springbootsetup.query;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
@Getter
public class Query {
    private String sortDir;
    private ZonedDateTime publishedAfter;

}
