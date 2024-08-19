package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostRepositoryStubImpl implements PostRepository {

    private final AtomicInteger postId = new AtomicInteger(0);
    private final ConcurrentHashMap<Long, Post> postTable = new ConcurrentHashMap<>();


    public List<Post> all() {
        return new ArrayList<>(postTable.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.of(Optional.ofNullable(postTable.get(id))
                .orElseThrow(() -> new NotFoundException("Не найден пост с id= " + id)));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long key = postId.incrementAndGet();
            post.setId(key);
            postTable.put(key, post);
            return post;
        }
        long postKey = post.getId();
        if (postTable.containsKey(postKey)) {
            postTable.put(postKey, post);
        } else {
            throw new NotFoundException("Нет поста для обновиления с id= " + postKey);
        }
        return post;
    }

    public void removeById(long id) {
        if (postTable.containsKey(id)) {
            postTable.remove(id);
        } else {
            throw new NotFoundException("При удалении не найден пост с id= " + id);
        }
    }
}