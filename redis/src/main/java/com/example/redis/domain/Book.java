package com.example.redis.domain;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash
@Builder
@Getter
public class Book {
    @Id
    Long id;

    String name;

    String writer;
}
