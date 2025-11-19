package teo.chat.service;

import org.springframework.stereotype.Service;
import teo.chat.entity.Post;
import teo.chat.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }
}
