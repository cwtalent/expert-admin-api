package com.expert.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MyBatisMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        
        // 处理 ExpertLoginLog 的 loginTime
        if (metaObject.hasSetter("loginTime") && metaObject.getValue("loginTime") == null) {
            this.strictInsertFill(metaObject, "loginTime", LocalDateTime.class, LocalDateTime.now());
        }
        
        // 处理 Vote 的 voteTime
        if (metaObject.hasSetter("voteTime") && metaObject.getValue("voteTime") == null) {
            this.strictInsertFill(metaObject, "voteTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}
