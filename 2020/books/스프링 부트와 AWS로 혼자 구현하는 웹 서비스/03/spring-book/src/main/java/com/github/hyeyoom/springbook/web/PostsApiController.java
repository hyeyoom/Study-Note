package com.github.hyeyoom.springbook.web;

import com.github.hyeyoom.springbook.service.posts.PostsService;
import com.github.hyeyoom.springbook.web.dto.PostResponseDto;
import com.github.hyeyoom.springbook.web.dto.PostsSaveRequestsDto;
import com.github.hyeyoom.springbook.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestsDto requestsDto) {
        return postsService.save(requestsDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
