package com.jial.controller;

import com.jial.event.EventProducer;
import com.jial.pojo.Event;
import com.jial.pojo.User;
import com.jial.pojo.VO;
import com.jial.service.LikeService;
import com.jial.utils.CommunityConstant;
import com.jial.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public VO like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();

        likeService.like(user.getId(), entityType, entityId, entityUserId);

        long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();

        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(CommunityConstant.TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }

        VO vo = new VO();
        vo.setObj(map);
        vo.setCode(0);

        return vo;
    }

}
