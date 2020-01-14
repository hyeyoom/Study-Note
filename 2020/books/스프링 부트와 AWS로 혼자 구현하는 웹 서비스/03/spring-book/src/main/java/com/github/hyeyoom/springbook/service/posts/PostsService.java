package com.github.hyeyoom.springbook.service.posts;

import com.github.hyeyoom.springbook.domain.posts.Posts;
import com.github.hyeyoom.springbook.domain.posts.PostsRepository;
import com.github.hyeyoom.springbook.web.dto.PostResponseDto;
import com.github.hyeyoom.springbook.web.dto.PostsSaveRequestsDto;
import com.github.hyeyoom.springbook.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestsDto requestsDto) {
        return postsRepository.save(requestsDto.toEntity()).getId();
    }

    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such posts: " + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        return post.getId();
    }

    public PostResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such posts: " + id));
        return new PostResponseDto(post);
    }
}
