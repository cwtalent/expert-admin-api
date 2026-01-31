package com.expert.admin.controller;

import com.expert.admin.entity.VotingSession;
import com.expert.admin.mapper.VotingSessionMapper;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/voting-sessions")
public class VotingSessionController {
    
    @Autowired
    private VotingSessionMapper mapper;
    
    @GetMapping
    public Map<String, Object> getAll() {
        List<VotingSession> list = mapper.selectList(null);
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/{guid}")
    public Map<String, Object> getByGuid(@PathVariable String guid) {
        VotingSession session = mapper.findBySessionGuid(guid);
        if (session == null) {
            return ResponseUtil.error(404, "投票会话不存在");
        }
        return ResponseUtil.success(session);
    }
}
