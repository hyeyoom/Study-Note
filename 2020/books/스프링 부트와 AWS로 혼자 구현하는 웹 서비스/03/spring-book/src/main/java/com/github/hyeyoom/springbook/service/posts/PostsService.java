package com.github.hyeyoom.springbook.service.posts;

import com.github.hyeyoom.springbook.domain.posts.Posts;
import com.github.hyeyoom.springbook.domain.posts.PostsRepository;
import com.github.hyeyoom.springbook.web.dto.PostResponseDto;
import com.github.hyeyoom.springbook.web.dto.PostsListResponseDto;
import com.github.hyeyoom.springbook.web.dto.PostsSaveRequestsDto;
import com.github.hyeyoom.springbook.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestsDto requestsDto) {
        return postsRepository.save(requestsDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such posts: " + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        return post.getId();
    }

    @Transactional
    public void delete(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such posts: " + id));
        postsRepository.delete(post);
    }

    public PostResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such posts: " + id));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

}
