package com.jial.service;

import com.jial.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                SetOperations setOperations = operations.opsForSet();
                ValueOperations valueOperations = operations.opsForValue();
                Boolean isMember = setOperations.isMember(entityLikeKey, userId);

                operations.multi();;
                if (isMember) {
                    setOperations.remove(entityLikeKey,userId);
                    valueOperations.decrement(userLikeKey);
                } else {
                    setOperations.add(entityLikeKey,userId);
                    valueOperations.increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    //查询某个实体的赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        SetOperations setOperations = redisTemplate.opsForSet();
        Boolean ismember = setOperations.isMember(entityLikeKey, userId);

        return ismember ? 1 : 0;
    }

    // 查询某个用户获得的赞个数
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}
