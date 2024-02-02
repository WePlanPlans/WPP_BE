package org.tenten.tentenbe.domain.tour.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.tenten.tentenbe.global.cache.RedisCache;

@Repository
@RequiredArgsConstructor
public class MessageProxyRepositoryImpl implements MessageProxyRepository{
    private final RedisCache redisCache;
}
