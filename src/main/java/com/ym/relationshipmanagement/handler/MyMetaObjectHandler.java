package com.ym.relationshipmanagement.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 字段自动填充处理器
 *
 * 在 INSERT 操作时，自动给 createdAt 和 updatedAt 赋值当前时间
 * 在 UPDATE 操作时，自动更新 updatedAt
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 执行 INSERT 时自动调用
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        // 参数1：实体类中的字段名
        // 参数2：要填充的值
        // 参数3：元对象
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
    }

    /**
     * 执行 UPDATE 时自动调用
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}
//MyMetaObjectHandler实现MetaObjectHandler这个接口
//@override重写，我知道是重写但是具体意思不知道为什么要重写。重写 = 父类/接口留了空位，你来填具体内容。
//创建一个返回值为void（空）insertFill方法，传入MetaObject类型的对象，不过这个MetaObject是哪里来的
//将现在的时间赋值给now变量
//this.strictInsertFill这个属性传入4个参数metaObject，字段名字（属性名一致），LocalDateTime.class（没看懂），now（没看懂）
//重写更新方法，与上面一致