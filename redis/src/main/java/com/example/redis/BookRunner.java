package com.example.redis;

import com.example.redis.domain.Book;
import com.example.redis.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookRunner implements ApplicationRunner {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Book book = Book.builder()
                .name("hello")
                .writer("yerin")
                .build();

        bookRepository.save(book);

        log.info(bookRepository.findById(book.getId()).get().getName());


    }
}
