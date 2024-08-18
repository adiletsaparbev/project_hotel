package com.example.it_proger.repo;

import com.example.it_proger.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
